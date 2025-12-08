/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.doctor;

import dao.*;
import model.*;
import services.*;
import session.UserSession;
import util.DateUtil;
import util.ValidationUtil;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;
import javax.swing.JOptionPane;
import ui.Forms.RecordVitalsForm;

/**
 *
 * @author pranjalpatil
 */
public class ViewAssignedComplaintsPanel extends javax.swing.JPanel {

    private ComplaintService complaintService;
    private PatientDAO patientDAO;
    private VitalSignsDAO vitalSignsDAO;
    private DefaultTableModel tableModel;
    private String currentFilter = "All";

    public ViewAssignedComplaintsPanel() {
        initComponents();

        this.complaintService = new ComplaintService();
        this.patientDAO = new PatientDAO();
        this.vitalSignsDAO = new VitalSignsDAO();

        this.tableModel = (DefaultTableModel) tblViewAssigned.getModel();

        setupTable();
        loadComplaints();
    }

    private void setupTable() {
        tableModel.setColumnIdentifiers(new Object[]{
            "ID",
            "PATIENT",
            "AGE",
            "CATEGORY",
            "PRIORITY",
            "STATUS",
            "DATE"
        });

        tblViewAssigned.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedComplaint();
                }
            }
        });
    }

    // Load complaints based on filter
    private void loadComplaints() {
        try {
            tableModel.setRowCount(0);

            List<Complaint> complaints = complaintService.getAssignedComplaints();

            if (complaints == null || complaints.isEmpty()) {
                return;
            }

            for (Complaint c : complaints) {
                boolean shouldAdd = false;

                if (currentFilter.equals("All")) {
                    shouldAdd = true;
                } else if (currentFilter.equals("Pending")) {
                    shouldAdd = c.getStatus().equals("ASSIGNED");
                } else if (currentFilter.equals("In Progress")) {
                    shouldAdd = c.getStatus().equals("IN_PROGRESS") || c.getStatus().equals("DIAGNOSED");
                }

                if (shouldAdd) {
                    Patient patient = patientDAO.getPatientByID(c.getPatientID());
                    String patientName = patient != null ? patient.getFullName() : "Unknown";
                    int age = patient != null ? patient.getAge() : 0;

                    Object[] row = new Object[]{
                        "C" + c.getComplaintID(),
                        patientName,
                        age,
                        c.getCategory(),
                        c.getPriority(),
                        c.getStatus(),
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
            int selectedRow = tblViewAssigned.getSelectedRow();

            if (selectedRow == -1) {
                clearDetailsPanel();
                return;
            }

            String complaintIDStr = (String) tblViewAssigned.getValueAt(selectedRow, 0);
            int complaintID = Integer.parseInt(complaintIDStr.substring(1));

            ComplaintDAO complaintDAO = new ComplaintDAO();
            Complaint complaint = complaintDAO.getComplaintByID(complaintID);

            if (complaint == null) {
                clearDetailsPanel();
                return;
            }

            Patient patient = patientDAO.getPatientByID(complaint.getPatientID());

            if (patient != null) {
                fieldPatient.setText(patient.getFullName());
                fieldPatient.setEditable(false);

                UserDAO userDAO = new UserDAO();
                User patientUser = userDAO.getUserByID(patient.getUserID());
                if (patientUser != null) {
                    fieldContact.setText(patientUser.getPhone());
                    fieldContact.setEditable(false);
                }
            }

            fieldCategory.setText(complaint.getCategory());
            fieldCategory.setEditable(false);

            fieldPriority.setText(complaint.getPriority());
            fieldPriority.setEditable(false);

            fldChiefComplaint.setText(complaint.getDescription());
            fldChiefComplaint.setEditable(false);

            if (complaint.getDescription().contains("Symptoms:")) {
                String[] parts = complaint.getDescription().split("Symptoms:");
                if (parts.length > 1) {
                    String symptoms = parts[1].split("Duration:")[0].trim();
                    fieldSymptoms.setText(symptoms);
                    fieldSymptoms.setEditable(false);
                }
            }

            if (complaint.getDescription().contains("Duration:")) {
                String[] parts = complaint.getDescription().split("Duration:");
                if (parts.length > 1) {
                    String duration = parts[1].split("days")[0].trim();
                    fieldDuratioj.setText(duration);
                    fieldDuratioj.setEditable(false);
                }
            }

            loadLatestVitals(complaintID);

        } catch (Exception e) {
            System.err.println("Error displaying complaint: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load latest vitals - FIXED to use complaintID
    private void loadLatestVitals(int complaintID) {
        try {
            List<VitalSigns> vitalsList = vitalSignsDAO.getVitalSignsByComplaintID(complaintID);

            if (vitalsList != null && !vitalsList.isEmpty()) {
                VitalSigns vitals = vitalsList.get(0);

                fieldBP.setText(vitals.getBloodPressure());
                fieldBP.setEditable(false);

                fieldHR.setText(vitals.getHeartRate() != null ? vitals.getHeartRate().toString() : "N/A");
                fieldHR.setEditable(false);

                fieldTemp.setText(vitals.getTemperature() != null ? vitals.getTemperature().toString() : "N/A");
                fieldTemp.setEditable(false);

                jTextField12.setText(vitals.getOxygenSaturation() != null ? vitals.getOxygenSaturation() + "%" : "N/A");
                jTextField12.setEditable(false);
            } else {
                fieldBP.setText("No vitals");
                fieldBP.setEditable(false);
                fieldHR.setText("N/A");
                fieldHR.setEditable(false);
                fieldTemp.setText("N/A");
                fieldTemp.setEditable(false);
                jTextField12.setText("N/A");
                jTextField12.setEditable(false);
            }

        } catch (Exception e) {
            System.err.println("Error loading vitals: " + e.getMessage());
        }
    }

    // Clear details panel
    private void clearDetailsPanel() {
        fieldPatient.setText("");
        fieldContact.setText("");
        fieldCategory.setText("");
        fieldPriority.setText("");
        fldChiefComplaint.setText("");
        fieldSymptoms.setText("");
        fieldDuratioj.setText("");
        fieldBP.setText("");
        fieldHR.setText("");
        fieldTemp.setText("");
        jTextField12.setText("");
    }

    // Search complaints
    private void searchComplaints() {
        try {
            String searchTerm = fieldSearch.getText().trim().toLowerCase();

            if (ValidationUtil.isEmpty(searchTerm)) {
                loadComplaints();
                return;
            }

            tableModel.setRowCount(0);

            List<Complaint> complaints = complaintService.getAssignedComplaints();

            if (complaints != null) {
                for (Complaint c : complaints) {
                    Patient patient = patientDAO.getPatientByID(c.getPatientID());
                    String patientName = patient != null ? patient.getFullName() : "";

                    String searchText = (patientName + " " + c.getDescription() + " " + c.getCategory()).toLowerCase();

                    if (searchText.contains(searchTerm)) {
                        int age = patient != null ? patient.getAge() : 0;

                        Object[] row = new Object[]{
                            "C" + c.getComplaintID(),
                            patientName,
                            age,
                            c.getCategory(),
                            c.getPriority(),
                            c.getStatus(),
                            DateUtil.formatDateTime(c.getCreatedDate())
                        };

                        tableModel.addRow(row);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error searching: " + e.getMessage());
        }
    }

    // Navigate back to dashboard
    private void navigateBackToDashboard() {
        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);

            if (window instanceof javax.swing.JFrame) {
                javax.swing.JFrame frame = (javax.swing.JFrame) window;
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new DoctorDashboard());
                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    // Add missing vitals fields declaration
    private javax.swing.JTextField fieldBP;
    private javax.swing.JTextField fieldHR;
    private javax.swing.JTextField fieldTemp;
    private javax.swing.JTextField jTextField12;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fieldSearch = new javax.swing.JTextField();
        cmboBoxFilter = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblViewAssigned = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fieldCategory = new javax.swing.JTextField();
        fldChiefComplaint = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fieldPatient = new javax.swing.JTextField();
        fieldContact = new javax.swing.JTextField();
        fieldPriority = new javax.swing.JTextField();
        fieldSymptoms = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fieldDuratioj = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(232, 244, 248));

        jPanel3.setBackground(new java.awt.Color(13, 115, 119));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(232, 244, 248));
        jLabel1.setText("MEDINEXUS");

        jLabel2.setForeground(new java.awt.Color(232, 244, 248));
        jLabel2.setText("Assigned Complaints");

        btnBack.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnBack.setForeground(new java.awt.Color(13, 115, 119));
        btnBack.setText("BACK");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(64, 64, 64))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(btnBack))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(13, 115, 119));
        jLabel7.setText("FILTER:");

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(13, 115, 119));
        jLabel8.setText("SEARCH:");

        fieldSearch.setBackground(new java.awt.Color(232, 244, 248));
        fieldSearch.setForeground(new java.awt.Color(13, 115, 119));
        fieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSearchActionPerformed(evt);
            }
        });

        cmboBoxFilter.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        cmboBoxFilter.setForeground(new java.awt.Color(13, 115, 119));
        cmboBoxFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "In Progress" }));
        cmboBoxFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmboBoxFilterActionPerformed(evt);
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
                .addComponent(cmboBoxFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(257, 257, 257)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(fieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(fieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmboBoxFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tblViewAssigned.setBackground(new java.awt.Color(13, 115, 119));
        tblViewAssigned.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PATIENT", "AGE", "CATEGORY", "PRIORITY", "STATUS", "DATE"
            }
        ));
        jScrollPane1.setViewportView(tblViewAssigned);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(13, 115, 119));
        jLabel9.setText("COMPLAINT DETAILS");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(13, 115, 119));
        jLabel3.setText("PATIENT");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(13, 115, 119));
        jLabel4.setText("CATEGORY");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(13, 115, 119));
        jLabel5.setText("CHIEF COMPLAINT");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(13, 115, 119));
        jLabel6.setText("CONTACT");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(13, 115, 119));
        jLabel10.setText("PRIORITY");

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(13, 115, 119));
        jLabel11.setText("SYMPTOMS");

        jLabel12.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(13, 115, 119));
        jLabel12.setText("DURATION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fieldDuratioj, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(fldChiefComplaint, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldCategory, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldPatient, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(24, 24, 24)
                        .addComponent(fieldContact))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldSymptoms))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(26, 26, 26)
                        .addComponent(fieldPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(fieldPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fieldCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(fieldPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fldChiefComplaint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(fieldSymptoms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(fieldDuratioj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton2.setBackground(new java.awt.Color(13, 115, 119));
        jButton2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(232, 244, 248));
        jButton2.setText("CREATE ASSESMENT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(13, 115, 119));
        jButton3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(232, 244, 248));
        jButton3.setText("Record Vitals");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(13, 115, 119));
        jButton4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(232, 244, 248));
        jButton4.setText("ASSIGN TO OTHER DOCTOR");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(13, 115, 119));
        jButton5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton5.setForeground(new java.awt.Color(232, 244, 248));
        jButton5.setText("VIEW HISTORY");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jButton2)
                        .addGap(243, 243, 243)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(322, 322, 322)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(325, 325, 325)
                    .addComponent(jButton5)
                    .addContainerGap(354, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(576, Short.MAX_VALUE)
                    .addComponent(jButton5)
                    .addGap(1, 1, 1)))
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = tblViewAssigned.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String complaintIDStr = (String) tblViewAssigned.getValueAt(selectedRow, 0);
        int complaintID = Integer.parseInt(complaintIDStr.substring(1));

        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);

            if (window instanceof javax.swing.JFrame) {
                javax.swing.JFrame frame = (javax.swing.JFrame) window;
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new CreateAssessmentPanel(complaintID));
                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            System.err.println("Error opening assessment panel: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        navigateBackToDashboard();
    }//GEN-LAST:event_btnBackActionPerformed

    private void cmboBoxFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmboBoxFilterActionPerformed
        currentFilter = (String) cmboBoxFilter.getSelectedItem();
        loadComplaints();
    }//GEN-LAST:event_cmboBoxFilterActionPerformed

    private void fieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSearchActionPerformed
        searchComplaints();
    }//GEN-LAST:event_fieldSearchActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = tblViewAssigned.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String complaintIDStr = (String) tblViewAssigned.getValueAt(selectedRow, 0);
        int complaintID = Integer.parseInt(complaintIDStr.substring(1));

        ComplaintDAO dao = new ComplaintDAO();
        Complaint complaint = dao.getComplaintByID(complaintID);

        if (complaint != null) {
            RecordVitalsForm vitalsForm = new RecordVitalsForm(complaintID, complaint.getPatientID());
            vitalsForm.setVisible(true);

            vitalsForm.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    displaySelectedComplaint();
                }
            });
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = tblViewAssigned.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Reassign Complaint\n\n(Feature not implemented)",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int selectedRow = tblViewAssigned.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Patient History\n\n(Panel not implemented)",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void fieldSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchComplaints();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> cmboBoxFilter;
    private javax.swing.JTextField fieldCategory;
    private javax.swing.JTextField fieldContact;
    private javax.swing.JTextField fieldDuratioj;
    private javax.swing.JTextField fieldPatient;
    private javax.swing.JTextField fieldPriority;
    private javax.swing.JTextField fieldSearch;
    private javax.swing.JTextField fieldSymptoms;
    private javax.swing.JTextField fldChiefComplaint;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblViewAssigned;
    // End of variables declaration//GEN-END:variables
}
