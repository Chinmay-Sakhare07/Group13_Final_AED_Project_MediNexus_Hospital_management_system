/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.auditor;

import dao.*;
import model.*;
import session.UserSession;
import util.DateUtil;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author anishagaikar
 */
public class AuditorDashboard extends javax.swing.JPanel {

    /**
     * Creates new form AuditorDashboard
     */
    private AuditLogDAO auditLogDAO;
    private UserDAO userDAO;
    private ComplaintDAO complaintDAO;
    private DefaultTableModel tableModel;

    public AuditorDashboard() {
        initComponents();
        this.auditLogDAO = new AuditLogDAO();
        this.userDAO = new UserDAO();
        this.complaintDAO = new ComplaintDAO();

        // Get table model
        this.tableModel = (DefaultTableModel) jTableRecentLogs.getModel();

        // Setup and load data
        setupTableColumns();
        loadDashboardData();
    }

    // Setup table columns for audit logs
    private void setupTableColumns() {
        tableModel.setColumnCount(0);
        tableModel.addColumn("Time");
        tableModel.addColumn("User");
        tableModel.addColumn("Action");
        tableModel.addColumn("Entity");
        tableModel.addColumn("Status");
        tableModel.addColumn("IP Address");
    }

    // Load all dashboard data
    private void loadDashboardData() {
        loadSystemStatistics();
        loadRecentAuditLogs();
        loadComplianceAlerts();
    }

    // Load system activity statistics
    private void loadSystemStatistics() {
        try {
            // Get all audit logs from database
            List<AuditLog> allLogs = auditLogDAO.getAllAuditLogs();

            // Initialize counters for last 24 hours
            int totalLogins = 0;
            int failedAttempts = 0;
            int dataAccessEvents = 0;
            int modifications = 0;
            int complianceViolations = 0;

            // Count different types of actions
            for (AuditLog log : allLogs) {
                if (DateUtil.isWithinLastHours(log.getTimestamp(), 24)) {
                    if (log.getAction().equals("LOGIN")) {
                        totalLogins++;
                    } else if (log.getAction().equals("FAILED_LOGIN")) {
                        failedAttempts++;
                    } else if (log.getAction().equals("VIEW") || log.getAction().equals("READ")) {
                        dataAccessEvents++;
                    } else if (log.getAction().equals("CREATE") || log.getAction().equals("UPDATE") || log.getAction().equals("DELETE")) {
                        modifications++;
                    }
                }
            }

            // Update UI labels with counts
            jLabelTotalLogins.setText(String.valueOf(totalLogins));
            jTextField1.setText(String.valueOf(failedAttempts));
            jTextField2.setText(String.valueOf(dataAccessEvents));
            jTextField3.setText(String.valueOf(modifications));
            jTextField4.setText(String.valueOf(complianceViolations));

            // Update audit summary counts
            jLabelActiveCount.setText("5");
            jLabelCompletedCount.setText("42");
            jLabelIssuesCount.setText(String.valueOf(complianceViolations));

        } catch (Exception e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load recent audit logs into table
    private void loadRecentAuditLogs() {
        try {
            // Clear existing table rows
            tableModel.setRowCount(0);

            // Get last 20 audit logs
            List<AuditLog> recentLogs = auditLogDAO.getRecentAuditLogs(20);

            // Add each log to table
            for (AuditLog log : recentLogs) {
                // Get username for this log
                User user = userDAO.getUserByID(log.getUserID());
                String username = user != null ? user.getUsername() : "Unknown";

                // Format timestamp as relative time
                String timeStr = DateUtil.getRelativeTime(log.getTimestamp());

                // Determine status based on action
                String status = log.getAction().contains("FAILED") ? "Failed" : "Success";

                // Create row data
                Object[] row = new Object[]{
                    timeStr,
                    username,
                    log.getAction(),
                    log.getEntityType() != null ? log.getEntityType() : "N/A",
                    status,
                    log.getIpAddress() != null ? log.getIpAddress() : "N/A"
                };

                // Add row to table
                tableModel.addRow(row);
            }

            System.out.println("Loaded " + recentLogs.size() + " audit logs");

        } catch (Exception e) {
            System.err.println("Error loading audit logs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load compliance alerts
    private void loadComplianceAlerts() {
        try {
            StringBuilder alerts = new StringBuilder();

            // Get recent logs to check for suspicious activity
            List<AuditLog> recentLogs = auditLogDAO.getRecentAuditLogs(100);

            // Count failed login attempts in last hour
            int failedLogins = 0;
            for (AuditLog log : recentLogs) {
                if (log.getAction().equals("FAILED_LOGIN")
                        && DateUtil.isWithinLastHours(log.getTimestamp(), 1)) {
                    failedLogins++;
                }
            }

            // Add alert if too many failed logins
            if (failedLogins > 3) {
                alerts.append("ALERT: ").append(failedLogins)
                        .append(" failed login attempts in the last hour\n\n");
            }

            // Show all clear message if no alerts
            if (alerts.length() == 0) {
                alerts.append("No compliance violations detected\n");
                alerts.append("All system activities are within normal parameters\n");
                alerts.append("No suspicious access patterns identified");
            } else {
                alerts.append("\nAll other systems operating normally");
            }

            // Update text area
            jTextAreaAlerts.setText(alerts.toString());

        } catch (Exception e) {
            System.err.println("Error loading compliance alerts: " + e.getMessage());
            jTextAreaAlerts.setText("Error loading alerts: " + e.getMessage());
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

        jButtonViewAllLogs = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelIssuesCount = new javax.swing.JLabel();
        jButtonGenerateReport = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRecentLogs = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButtonLogout = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelActiveCount = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelCompletedCount = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabelTotalLogins = new javax.swing.JLabel();
        jLabelFailedAttempts = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaAlerts = new javax.swing.JTextArea();

        jButtonViewAllLogs.setText("VIEW ALL LOGS");
        jButtonViewAllLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewAllLogsActionPerformed(evt);
            }
        });

        jLabelIssuesCount.setText("Issues Found");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabelIssuesCount)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelIssuesCount)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jButtonGenerateReport.setText("GENERATE AUDIT REPORT");
        jButtonGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateReportActionPerformed(evt);
            }
        });

        jTableRecentLogs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Order", "Patient", "Drug", "Qty", "Dosage", "Prescribed"
            }
        ));
        jScrollPane1.setViewportView(jTableRecentLogs);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("MediNexus | Audit Portal");

        jButtonLogout.setText("Logout");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setText("AUDIT SUMMARY");

        jLabelActiveCount.setText("Active Audits");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabelActiveCount)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelActiveCount)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jLabelCompletedCount.setText("Completed Audits");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCompletedCount)
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCompletedCount)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel7.setText("SYSTEM ACTIVITY");

        jLabelTotalLogins.setText("Total Logins:");

        jLabelFailedAttempts.setText("Failed Login Attempts:");

        jLabel10.setText("Data Access Events:");

        jLabel11.setText("Modifications:");

        jLabel12.setText("Compliance Violations:");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabelTotalLogins))
                    .addComponent(jLabelFailedAttempts)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addGap(102, 102, 102)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(61, 61, 61)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalLogins)
                    .addComponent(jLabel11)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFailedAttempts)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel6.setText("RECENT AUDIT LOGS");

        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel13.setText("COMPLIANCE ALERTS  ");

        jTextAreaAlerts.setColumns(20);
        jTextAreaAlerts.setRows(5);
        jScrollPane2.setViewportView(jTextAreaAlerts);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(164, 164, 164)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(jButtonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel13)
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jButtonViewAllLogs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonGenerateReport)
                .addGap(174, 174, 174))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonLogout))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonViewAllLogs)
                    .addComponent(jButtonGenerateReport))
                .addGap(54, 54, 54))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewAllLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewAllLogsActionPerformed
        try {
            // Clear table
            tableModel.setRowCount(0);

            // Get all audit logs instead of just recent
            List<AuditLog> allLogs = auditLogDAO.getAllAuditLogs();

            // Populate table with all logs
            for (AuditLog log : allLogs) {
                User user = userDAO.getUserByID(log.getUserID());
                String username = user != null ? user.getUsername() : "Unknown";
                String timeStr = DateUtil.formatDateTime(log.getTimestamp());
                String status = log.getAction().contains("FAILED") ? "Failed" : "Success";

                Object[] row = new Object[]{
                    timeStr,
                    username,
                    log.getAction(),
                    log.getEntityType() != null ? log.getEntityType() : "N/A",
                    status,
                    log.getIpAddress() != null ? log.getIpAddress() : "N/A"
                };

                tableModel.addRow(row);
            }

            // Show confirmation message
            JOptionPane.showMessageDialog(this,
                    "Loaded " + allLogs.size() + " audit logs",
                    "All Logs",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonViewAllLogsActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButtonGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateReportActionPerformed
        try {
            Employee currentAuditor = UserSession.getInstance().getCurrentEmployee();

            if (currentAuditor == null) {
                JOptionPane.showMessageDialog(this, "Auditor not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder reportContent = new StringBuilder();
            reportContent.append("AUDIT REPORT\n");
            reportContent.append("Generated by: ").append(currentAuditor.getFullName()).append("\n");
            reportContent.append("Date: ").append(DateUtil.getCurrentDateTime()).append("\n");
            reportContent.append("=".repeat(50)).append("\n\n");
            reportContent.append("SYSTEM ACTIVITY (Last 24 Hours)\n");
            reportContent.append("-".repeat(50)).append("\n");
            reportContent.append("Total Logins: ").append(jLabelTotalLogins.getText()).append("\n");
            reportContent.append("Failed Attempts: ").append(jTextField1.getText()).append("\n");
            reportContent.append("Data Access Events: ").append(jTextField2.getText()).append("\n");
            reportContent.append("Modifications: ").append(jTextField3.getText()).append("\n");
            reportContent.append("Compliance Violations: ").append(jTextField4.getText()).append("\n");

            ReportDAO reportDAO = new ReportDAO();
            Report report = new Report();
            report.setGeneratedBy(currentAuditor.getEmployeeID());
            report.setReportType("AUDIT_SUMMARY");
            report.setReportTitle("Audit Report - " + DateUtil.getCurrentDate());
            report.setReportContent(reportContent.toString());

            boolean saved = reportDAO.createReport(report);

            if (saved) {
                JOptionPane.showMessageDialog(this,
                        "Report generated successfully!\n\nReport ID: R" + report.getReportID(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate report", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonGenerateReportActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Clear user session
            UserSession.getInstance().clearSession();

            // If this panel is inside a frame, close the parent frame
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose();

            // Open login window when ready
            //new LoginFrame().setVisible(true);
            System.out.println("Auditor logged out");
        }
    }//GEN-LAST:event_jButtonLogoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonGenerateReport;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonViewAllLogs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelActiveCount;
    private javax.swing.JLabel jLabelCompletedCount;
    private javax.swing.JLabel jLabelFailedAttempts;
    private javax.swing.JLabel jLabelIssuesCount;
    private javax.swing.JLabel jLabelTotalLogins;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableRecentLogs;
    private javax.swing.JTextArea jTextAreaAlerts;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
