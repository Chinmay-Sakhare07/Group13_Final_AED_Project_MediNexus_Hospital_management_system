/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.doctor;

/**
 *
 * @author pranjalpatil
 */
import dao.ComplaintDAO;
import dao.PatientDAO;
import model.Complaint;
import model.Employee;
import model.Patient;
import services.ComplaintService;
import services.NotificationService;
import session.UserSession;
import util.DateUtil;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DoctorDashboard extends javax.swing.JPanel {

    /**
     * Creates new form DoctorDashboard
     */
    private ComplaintService complaintService;
    private NotificationService notificationService;
    private DefaultTableModel tableModel;
    private Employee currentDoctor;

    public DoctorDashboard() {
        initComponents();
        this.complaintService = new ComplaintService();
        this.notificationService = new NotificationService();

        this.currentDoctor = UserSession.getInstance().getCurrentEmployee();

        setupTable();
        loadDashboardData();
    }

    private void setupTable() {
        this.tableModel = (DefaultTableModel) tblDoctorDashboard.getModel();
        // Reset columns to ensure correct headers and structure
        tableModel.setColumnIdentifiers(new Object[]{"ID", "PATIENT", "CATEGORY", "PRIORITY", "STATUS", "DATE"});
    }

    private void loadDashboardData() {
        if (currentDoctor == null) {
            // Handle case where session is invalid
            JOptionPane.showMessageDialog(this, "Doctor session not found. Please re-login.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadDashboardSummary();
        loadAssignedComplaints();
        loadNotifications();
    }

    private void loadDashboardSummary() {
        try {
            // Get all complaints assigned to the current doctor
            List<Complaint> assignedComplaints = complaintService.getAssignedComplaints();

            if (assignedComplaints == null) {
                return;
            }

            long pendingCount = assignedComplaints.stream()
                    .filter(c -> c.getStatus().equals("ASSIGNED")) // New case ready for assessment
                    .count();

            long inProgressCount = assignedComplaints.stream()
                    .filter(c -> c.getStatus().equals("IN_PROGRESS") || c.getStatus().equals("DIAGNOSED"))
                    .count();

            long completedCount = assignedComplaints.stream()
                    .filter(c -> c.getStatus().equals("TREATED") || c.getStatus().equals("CLOSED"))
                    .count();

            // Update the JTextFields in the dashboard design
            fieldPending.setText(String.valueOf(pendingCount));
            fieldInProgress.setText(String.valueOf(inProgressCount));
            fieldCompleted.setText(String.valueOf(completedCount));

        } catch (Exception e) {
            System.err.println("Error loading dashboard summary: " + e.getMessage());
        }
    }

    private void loadAssignedComplaints() {
        try {
            tableModel.setRowCount(0); // Clear old data

            // Get assigned complaints (from ComplaintService)
            List<Complaint> complaints = complaintService.getAssignedComplaints();

            if (complaints == null) {
                return;
            }

            // Get a list of patient IDs to look up patient names efficiently
            PatientDAO patientDAO = new PatientDAO();

            for (Complaint c : complaints) {
                // Lookup patient name (requires PatientDAO lookup)
                Patient patient = patientDAO.getPatientByID(c.getPatientID());
                String patientName = (patient != null) ? patient.getFullName() : "Unknown Patient";

                // Add row to the table
                tableModel.addRow(new Object[]{
                    "C" + c.getComplaintID(),
                    patientName,
                    c.getCategory(),
                    c.getPriority(),
                    c.getStatus(),
                    DateUtil.formatDateTime(c.getCreatedDate())
                });
            }
            System.out.println("Loaded " + complaints.size() + " assigned complaints for Dr. " + currentDoctor.getFullName());

        } catch (Exception e) {
            System.err.println("Error loading assigned complaints: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadNotifications() {
        try {
            // Notifications are generally loaded by UserID, which is linked to the Employee
            List<model.Notification> notifications = notificationService.getUnreadNotifications();

            if (notifications == null || notifications.isEmpty()) {
                fieldRecentNotifications1.setText("No new alerts or messages.");
                fieldRecentNotifications2.setText("");
                return;
            }

            // Display the top two unread notifications in the fields
            fieldRecentNotifications1.setText("1. " + notifications.get(0).getMessage());

            if (notifications.size() > 1) {
                fieldRecentNotifications2.setText("2. " + notifications.get(1).getMessage());
            } else {
                fieldRecentNotifications2.setText("");
            }

        } catch (Exception e) {
            System.err.println("Error loading doctor notifications: " + e.getMessage());
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fieldPending = new javax.swing.JTextField();
        fieldInProgress = new javax.swing.JTextField();
        fieldCompleted = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDoctorDashboard = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnViewAllComplaints = new javax.swing.JButton();
        lblCreateAssesment = new javax.swing.JButton();
        btnReferSpecialist = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        fieldRecentNotifications1 = new javax.swing.JTextField();
        fieldRecentNotifications2 = new javax.swing.JTextField();

        jPanel1.setBackground(new java.awt.Color(232, 244, 248));

        jPanel3.setBackground(new java.awt.Color(13, 115, 119));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(232, 244, 248));
        jLabel1.setText("MEDINEXUS");

        jLabel2.setForeground(new java.awt.Color(232, 244, 248));
        jLabel2.setText("Doctor Portal");

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

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(13, 115, 119));
        jLabel3.setText("QUICK OPTIONS");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(13, 115, 119));
        jLabel4.setText("PENDING");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(13, 115, 119));
        jLabel5.setText("IN PROGRESS");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(13, 115, 119));
        jLabel6.setText("COMPLETED");

        tblDoctorDashboard.setBackground(new java.awt.Color(13, 115, 119));
        tblDoctorDashboard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PATIENT", "CATEGORY", "PRIORITY", "STATUS", "DATE"
            }
        ));
        jScrollPane1.setViewportView(tblDoctorDashboard);

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(13, 115, 119));
        jLabel7.setText("DASHBOARD SUMMARY");

        btnViewAllComplaints.setBackground(new java.awt.Color(13, 115, 119));
        btnViewAllComplaints.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnViewAllComplaints.setForeground(new java.awt.Color(232, 244, 248));
        btnViewAllComplaints.setText("VIEW ALL COMPLAINTS");
        btnViewAllComplaints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllComplaintsActionPerformed(evt);
            }
        });

        lblCreateAssesment.setBackground(new java.awt.Color(13, 115, 119));
        lblCreateAssesment.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCreateAssesment.setForeground(new java.awt.Color(232, 244, 248));
        lblCreateAssesment.setText("CREATE ASSESMENT");
        lblCreateAssesment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblCreateAssesmentActionPerformed(evt);
            }
        });

        btnReferSpecialist.setBackground(new java.awt.Color(13, 115, 119));
        btnReferSpecialist.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnReferSpecialist.setForeground(new java.awt.Color(232, 244, 248));
        btnReferSpecialist.setText("REFER SPECIALIST");
        btnReferSpecialist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReferSpecialistActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(13, 115, 119));
        jLabel8.setText("RECENT NOTIFICATIONS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(fieldPending, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(154, 154, 154)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldInProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(fieldCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(145, 145, 145))
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btnViewAllComplaints)
                        .addGap(78, 78, 78)
                        .addComponent(lblCreateAssesment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(btnReferSpecialist)
                        .addGap(118, 118, 118))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(fieldRecentNotifications1)
                    .addComponent(fieldRecentNotifications2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldPending, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldInProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewAllComplaints)
                    .addComponent(lblCreateAssesment)
                    .addComponent(btnReferSpecialist))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fieldRecentNotifications1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fieldRecentNotifications2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
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

    private void btnReferSpecialistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReferSpecialistActionPerformed
        //switchPanel(new ReferSpecialistPanel(diagnosis.getDiagnosisID()));
    }//GEN-LAST:event_btnReferSpecialistActionPerformed

    private void lblCreateAssesmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblCreateAssesmentActionPerformed
        int selectedRow = tblDoctorDashboard.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint from the table to create an assessment.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String complaintIDStr = (String) tableModel.getValueAt(selectedRow, 0);
        int complaintID = Integer.parseInt(complaintIDStr.substring(1));

        switchPanel(new CreateAssessmentPanel(complaintID));
    }//GEN-LAST:event_lblCreateAssesmentActionPerformed

    private void btnViewAllComplaintsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllComplaintsActionPerformed
        switchPanel(new ViewAssignedComplaintsPanel());
    }//GEN-LAST:event_btnViewAllComplaintsActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.getInstance().clearSession();
            // Assume closing the current frame opens the LoginJFrame_Main
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
                // Optional: Re-launch Login frame (assuming you have one, or rely on application closure)
            }
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void switchPanel(javax.swing.JPanel newPanel) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window instanceof javax.swing.JFrame) {
            javax.swing.JFrame frame = (javax.swing.JFrame) window;
            frame.getContentPane().removeAll();
            frame.getContentPane().add(newPanel);
            frame.revalidate();
            frame.repaint();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnReferSpecialist;
    private javax.swing.JButton btnViewAllComplaints;
    private javax.swing.JTextField fieldCompleted;
    private javax.swing.JTextField fieldInProgress;
    private javax.swing.JTextField fieldPending;
    private javax.swing.JTextField fieldRecentNotifications1;
    private javax.swing.JTextField fieldRecentNotifications2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lblCreateAssesment;
    private javax.swing.JTable tblDoctorDashboard;
    // End of variables declaration//GEN-END:variables
}
