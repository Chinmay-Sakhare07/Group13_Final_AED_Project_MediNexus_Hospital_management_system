/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.patient;

import dao.*;
import model.*;
import services.*;
import session.UserSession;
import util.DateUtil;
import util.ValidationUtil;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author pranjalpatil
 */
public class ViewComplaintPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewComplaintPanel
     */
    private PatientDashboard parentDashboard;

    // Services and DAOs
    private ComplaintService complaintService;
    private AssessmentDAO assessmentDAO;
    private DiagnosisDAO diagnosisDAO;
    private TreatmentDAO treatmentDAO;
    private EmployeeDAO employeeDAO;
    private DefaultTableModel tableModel;

    // Current filter
    private String currentFilter = "ALL";

    public ViewComplaintPanel(PatientDashboard parent) {
        this.parentDashboard = parent;
        initComponents();

        // Initialize services
        this.complaintService = new ComplaintService();
        this.assessmentDAO = new AssessmentDAO();
        this.diagnosisDAO = new DiagnosisDAO();
        this.treatmentDAO = new TreatmentDAO();
        this.employeeDAO = new EmployeeDAO();

        // Get table model
        this.tableModel = (DefaultTableModel) jTable2.getModel();

        // Setup table
        setupTable();

        // Load complaints
        loadComplaints();
    }

    // Setup table columns
    private void setupTable() {
        tableModel.setColumnIdentifiers(new Object[]{
            "ID",
            "CATEGORY",
            "STATUS",
            "PRIORITY",
            "ASSIGNED TO",
            "DATE"
        });

        // Add table selection listener
        jTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedComplaint();
                }
            }
        });
    }

    // Load complaints based on current filter
    private void loadComplaints() {
        try {
            // Clear table
            tableModel.setRowCount(0);

            // Get all complaints for patient
            List<Complaint> allComplaints = complaintService.getMyComplaints();

            if (allComplaints == null || allComplaints.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No complaints found", "Info", JOptionPane.INFORMATION_MESSAGE);
                clearDetailsPanel();
                return;
            }

            // Filter complaints based on current filter
            for (Complaint c : allComplaints) {
                boolean shouldAdd = false;

                if (currentFilter.equals("ALL")) {
                    shouldAdd = true;
                } else if (currentFilter.equals("ACTIVE")) {
                    // Active = not CLOSED
                    shouldAdd = !c.getStatus().equals("CLOSED");
                } else if (currentFilter.equals("CLOSED")) {
                    shouldAdd = c.getStatus().equals("CLOSED");
                }

                if (shouldAdd) {
                    // Get assigned doctor name
                    String assignedTo = "Pending";
                    if (c.getAssignedDoctorID() != null) {
                        Employee doctor = employeeDAO.getEmployeeByID(c.getAssignedDoctorID());
                        if (doctor != null) {
                            assignedTo = "Dr. " + doctor.getFullName();
                        }
                    }

                    Object[] row = new Object[]{
                        "C" + c.getComplaintID(),
                        c.getCategory(),
                        c.getStatus(),
                        c.getPriority(),
                        assignedTo,
                        DateUtil.formatDateTime(c.getCreatedDate())
                    };

                    tableModel.addRow(row);
                }
            }

            System.out.println("Loaded complaints with filter: " + currentFilter);

        } catch (Exception e) {
            System.err.println("Error loading complaints: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Display selected complaint details
    private void displaySelectedComplaint() {
        try {
            int selectedRow = jTable2.getSelectedRow();

            if (selectedRow == -1) {
                clearDetailsPanel();
                return;
            }

            // Get complaint ID from table
            String complaintIDStr = (String) jTable2.getValueAt(selectedRow, 0);
            int complaintID = Integer.parseInt(complaintIDStr.substring(1)); // Remove 'C' prefix

            // Get complaint details using DAO (READ operation)
            ComplaintDAO complaintDAO = new ComplaintDAO();
            Complaint complaint = complaintDAO.getComplaintByID(complaintID);

            if (complaint == null) {
                clearDetailsPanel();
                return;
            }

            // Display basic info
            jTextField2.setText(complaint.getCategory());
            jTextField2.setEditable(false);

            jTextField3.setText(complaint.getStatus());
            jTextField3.setEditable(false);

            jTextField4.setText(complaint.getDescription());
            jTextField4.setEditable(false);

            // Get assigned doctor
            String doctorName = "Not assigned";
            if (complaint.getAssignedDoctorID() != null) {
                Employee doctor = employeeDAO.getEmployeeByID(complaint.getAssignedDoctorID());
                if (doctor != null) {
                    doctorName = "Dr. " + doctor.getFullName();
                }
            }
            jTextField5.setText(doctorName);
            jTextField5.setEditable(false);

            // Get diagnosis if exists
            Assessment assessment = assessmentDAO.getAssessmentByComplaintID(complaintID);
            if (assessment != null) {
                Diagnosis diagnosis = diagnosisDAO.getDiagnosisByAssessmentID(assessment.getAssessmentID());

                if (diagnosis != null) {
                    jTextField6.setText(diagnosis.getDiagnosisName() + " - " + diagnosis.getDescription());
                    jTextField6.setEditable(false);

                    // Get treatment if exists
                    Treatment treatment = treatmentDAO.getTreatmentByDiagnosisID(diagnosis.getDiagnosisID());
                    if (treatment != null) {
                        jTextField7.setText(treatment.getTreatmentPlan());
                        jTextField7.setEditable(false);
                    } else {
                        jTextField7.setText("No treatment plan yet");
                        jTextField7.setEditable(false);
                    }
                } else {
                    jTextField6.setText("No diagnosis yet");
                    jTextField6.setEditable(false);
                    jTextField7.setText("No treatment plan yet");
                    jTextField7.setEditable(false);
                }
            } else {
                jTextField6.setText("Assessment pending");
                jTextField6.setEditable(false);
                jTextField7.setText("No treatment plan yet");
                jTextField7.setEditable(false);
            }

            // Update progress bar based on status
            updateProgressBar(complaint.getStatus());

        } catch (Exception e) {
            System.err.println("Error displaying complaint: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update progress bar based on complaint status
    private void updateProgressBar(String status) {
        int progress = 0;

        switch (status) {
            case "SUBMITTED":
                progress = 20;
                break;
            case "ASSIGNED":
                progress = 40;
                break;
            case "IN_PROGRESS":
                progress = 60;
                break;
            case "DIAGNOSED":
                progress = 80;
                break;
            case "TREATED":
            case "CLOSED":
                progress = 100;
                break;
            default:
                progress = 0;
        }

        jProgressBar1.setValue(progress);
    }

    // Clear details panel
    private void clearDetailsPanel() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jProgressBar1.setValue(0);
    }

    // Search complaints
    private void searchComplaints() {
        try {
            String searchTerm = jTextField1.getText().trim().toLowerCase();

            if (ValidationUtil.isEmpty(searchTerm)) {
                // If search is empty, reload all
                loadComplaints();
                return;
            }

            // Clear table
            tableModel.setRowCount(0);

            // Get all complaints
            List<Complaint> allComplaints = complaintService.getMyComplaints();

            if (allComplaints != null) {
                // Filter by search term
                for (Complaint c : allComplaints) {
                    // Search in description, category, or status
                    String searchText = (c.getDescription() + " " + c.getCategory() + " " + c.getStatus()).toLowerCase();

                    if (searchText.contains(searchTerm)) {
                        String assignedTo = "Pending";
                        if (c.getAssignedDoctorID() != null) {
                            Employee doctor = employeeDAO.getEmployeeByID(c.getAssignedDoctorID());
                            if (doctor != null) {
                                assignedTo = "Dr. " + doctor.getFullName();
                            }
                        }

                        Object[] row = new Object[]{
                            "C" + c.getComplaintID(),
                            c.getCategory(),
                            c.getStatus(),
                            c.getPriority(),
                            assignedTo,
                            DateUtil.formatDateTime(c.getCreatedDate())
                        };

                        tableModel.addRow(row);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error searching complaints: " + e.getMessage());
        }
    }

    // Navigate back to dashboard
    private void navigateBackToDashboard() {
        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);

            if (window instanceof javax.swing.JFrame) {
                javax.swing.JFrame frame = (javax.swing.JFrame) window;

                frame.getContentPane().removeAll();

                if (parentDashboard != null) {
                    parentDashboard.refreshDashboard();
                    frame.getContentPane().add(parentDashboard);
                } else {
                    frame.getContentPane().add(new PatientDashboard());
                }

                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(232, 244, 248));

        jPanel2.setBackground(new java.awt.Color(232, 244, 248));

        jPanel3.setBackground(new java.awt.Color(13, 115, 119));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(232, 244, 248));
        jLabel1.setText("MEDINEXUS");

        jLabel2.setForeground(new java.awt.Color(232, 244, 248));
        jLabel2.setText("My Complaint");

        jButton1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(13, 115, 119));
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 410, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(64, 64, 64))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jButton1))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(13, 115, 119));
        jLabel7.setText("FILTER:");

        jToggleButton1.setBackground(new java.awt.Color(232, 244, 248));
        jToggleButton1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(13, 115, 119));
        jToggleButton1.setText("ALL");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setBackground(new java.awt.Color(232, 244, 248));
        jToggleButton2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(13, 115, 119));
        jToggleButton2.setText("ACTIVE");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setBackground(new java.awt.Color(232, 244, 248));
        jToggleButton3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(13, 115, 119));
        jToggleButton3.setText("CLOSED");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(13, 115, 119));
        jLabel8.setText("SEARCH:");

        jTextField1.setBackground(new java.awt.Color(232, 244, 248));
        jTextField1.setForeground(new java.awt.Color(13, 115, 119));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton3)
                .addGap(149, 149, 149)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2)
                    .addComponent(jToggleButton3)
                    .addComponent(jLabel8)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "CATEGORY", "STATUS", "PRIORITY", "ASSIGNED TO ", "DATE"
            }
        ));
        jScrollPane1.setViewportView(jTable2);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(13, 115, 119));
        jLabel3.setText("CATEGORY");

        jTextField2.setBackground(new java.awt.Color(232, 244, 248));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(13, 115, 119));
        jLabel4.setText("STATUS");

        jTextField3.setBackground(new java.awt.Color(232, 244, 248));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(13, 115, 119));
        jLabel5.setText("DESCRIPTION:");

        jTextField4.setBackground(new java.awt.Color(232, 244, 248));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(13, 115, 119));
        jLabel6.setText("ASSIGNED DOCTOR");

        jTextField5.setBackground(new java.awt.Color(232, 244, 248));
        jTextField5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(13, 115, 119));

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(13, 115, 119));
        jLabel9.setText("DIAGNOSIS");

        jTextField6.setBackground(new java.awt.Color(255, 204, 153));

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(13, 115, 119));
        jLabel10.setText("TREATMENT PLAN");

        jTextField7.setBackground(new java.awt.Color(204, 255, 204));

        jProgressBar1.setBackground(new java.awt.Color(51, 153, 255));
        jProgressBar1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(13, 115, 119));
        jLabel11.setText("PROGRESS");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(82, 82, 82)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(67, 67, 67)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(13, 115, 119));
        jButton3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(232, 244, 248));
        jButton3.setText("VIEW DIAGNOSIS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(279, 279, 279)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        navigateBackToDashboard();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint first", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Diagnosis is already shown in jTextField6
        if (ValidationUtil.isEmpty(jTextField6.getText()) || jTextField6.getText().equals("No diagnosis yet") || jTextField6.getText().equals("Assessment pending")) {
            JOptionPane.showMessageDialog(this, "No diagnosis available for this complaint yet", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Diagnosis Details:\n\n" + jTextField6.getText() + "\n\nTreatment:\n" + jTextField7.getText(),
                    "Diagnosis",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        currentFilter = "ALL";
        jToggleButton1.setSelected(true);
        jToggleButton2.setSelected(false);
        jToggleButton3.setSelected(false);
        loadComplaints();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        currentFilter = "ACTIVE";
        jToggleButton1.setSelected(false);
        jToggleButton2.setSelected(true);
        jToggleButton3.setSelected(false);
        loadComplaints();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        currentFilter = "CLOSED";
        jToggleButton1.setSelected(false);
        jToggleButton2.setSelected(false);
        jToggleButton3.setSelected(true);
        loadComplaints();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchComplaints();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchComplaints();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    // End of variables declaration//GEN-END:variables
}
