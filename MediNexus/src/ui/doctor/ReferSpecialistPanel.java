/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.doctor;

import dao.*;
import java.util.List;
import model.*;
import services.*;
import session.UserSession;
import util.ValidationUtil;
import javax.swing.JOptionPane;

/**
 *
 * @author pranjalpatil
 */
public class ReferSpecialistPanel extends javax.swing.JPanel {

    private DoctorService doctorService;
    private DiagnosisDAO diagnosisDAO;
    private AssessmentDAO assessmentDAO;
    private ComplaintDAO complaintDAO;
    private PatientDAO patientDAO;
    private EmployeeDAO employeeDAO;
    private int diagnosisID;
    private Diagnosis currentDiagnosis;

    public ReferSpecialistPanel() {
        initComponents();
        initializeServices();
    }

    public ReferSpecialistPanel(int diagnosisID) {
        this.diagnosisID = diagnosisID;
        initComponents();
        initializeServices();
        loadDiagnosisInfo();
    }

    private void initializeServices() {
        this.doctorService = new DoctorService();
        this.diagnosisDAO = new DiagnosisDAO();
        this.assessmentDAO = new AssessmentDAO();
        this.complaintDAO = new ComplaintDAO();
        this.patientDAO = new PatientDAO();
        this.employeeDAO = new EmployeeDAO();

        loadSpecialistsList();
    }

    // Load diagnosis information
    private void loadDiagnosisInfo() {
        try {
            // Get diagnosis
            currentDiagnosis = diagnosisDAO.getDiagnosisByID(diagnosisID);

            if (currentDiagnosis == null) {
                JOptionPane.showMessageDialog(this, "Diagnosis not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Display diagnosis
            fieldDiagnosis.setText(currentDiagnosis.getDiagnosisName());
            fieldDiagnosis.setEditable(false);

            // Get patient info
            Assessment assessment = assessmentDAO.getAssessmentByID(currentDiagnosis.getAssessmentID());

            if (assessment != null) {
                Complaint complaint = complaintDAO.getComplaintByID(assessment.getComplaintID());

                if (complaint != null) {
                    Patient patient = patientDAO.getPatientByID(complaint.getPatientID());

                    if (patient != null) {
                        fieldPatient.setText(patient.getFullName());
                        fieldPatient.setEditable(false);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading diagnosis info: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load list of available specialists
    private void loadSpecialistsList() {
        try {
            // Get all specialists
            List<Employee> specialists = employeeDAO.getEmployeesBySpecialization("Cardiology");

            // Clear comboboxes
            comboBoxPreferredSpec.removeAllItems();
            comboBoxPreferredSpec2.removeAllItems();

            // Add specialists to comboboxes
            for (Employee specialist : specialists) {
                String displayName = "Dr. " + specialist.getFullName();
                comboBoxPreferredSpec.addItem(displayName);
                comboBoxPreferredSpec2.addItem(displayName);
            }

            // Add default option
            if (specialists.isEmpty()) {
                comboBoxPreferredSpec.addItem("No specialists available");
                comboBoxPreferredSpec2.addItem("No specialists available");
            }

        } catch (Exception e) {
            System.err.println("Error loading specialists: " + e.getMessage());
        }
    }

    // Get selected urgency
    private String getSelectedUrgency() {
        if (jCheckBox2.isSelected()) {
            return "Routine";
        }
        if (jCheckBox3.isSelected()) {
            return "Urgent";
        }
        if (jCheckBox4.isSelected()) {
            return "Emergency";
        }
        return "Routine";
    }

    // Update urgency checkboxes (mutually exclusive)
    private void updateUrgencyCheckboxes(javax.swing.JCheckBox selected) {
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        selected.setSelected(true);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fieldPatient = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        fieldDiagnosis = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ComboBoxSpecialityRequires = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        comboBoxPreferredSpec = new javax.swing.JComboBox<>();
        comboBoxPreferredSpec2 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReferral = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaClinicalSummanry = new javax.swing.JTextArea();
        btnSubmitReferal = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 244, 248));

        jPanel3.setBackground(new java.awt.Color(13, 115, 119));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(232, 244, 248));
        jLabel1.setText("MEDINEXUS");

        jLabel2.setForeground(new java.awt.Color(232, 244, 248));
        jLabel2.setText("Refer to Specialist");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(13, 115, 119));
        jLabel7.setText("PATIENT AND DIAGNOSIS INFORMATION");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(13, 115, 119));
        jLabel3.setText("PATIENT");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(13, 115, 119));
        jLabel4.setText("DIAGNOSIS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(fieldPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(fieldDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fieldPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(fieldDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(13, 115, 119));
        jLabel5.setText("REFERRAL DETAILS");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(13, 115, 119));
        jLabel6.setText("SPECIALITY REQUIRED");

        ComboBoxSpecialityRequires.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        ComboBoxSpecialityRequires.setForeground(new java.awt.Color(13, 115, 119));
        ComboBoxSpecialityRequires.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cardiology (Heart & Cardiovascular)", "Pulmonology (Lungs & Respiratory)", "Neurology (Brain & Nervous System)", "Orthopedics (Bones & Joints)", "Gastroenterology (Digestive System)", "Endocrinology (Hormones & Metabolism)", "Nephrology (Kidneys)", "Oncology (Cancer)", "Dermatology (Skin)", "Psychiatry (Mental Health)", "General Surgery", "Cardiothoracic Surgery", "Neurosurgery", "Orthopedic Surgery", "Plastic Surgery", "Ophthalmology (Eyes)", "ENT (Ear, Nose, Throat)", "Urology (Urinary System)", "Gynecology (Women's Health)", "Pediatrics (Children)", "Geriatrics (Elderly)", "Rheumatology (Autoimmune & Joint disorders)", "Hematology (Blood disorders)", "Infectious Disease", "Emergency Medicine", "Anesthesiology", "Radiology", "Pathology" }));

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(13, 115, 119));
        jLabel8.setText("PREFERRED SPECIALITY");

        comboBoxPreferredSpec.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        comboBoxPreferredSpec.setForeground(new java.awt.Color(13, 115, 119));

        comboBoxPreferredSpec2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        comboBoxPreferredSpec2.setForeground(new java.awt.Color(13, 115, 119));

        jCheckBox1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(13, 115, 119));
        jCheckBox1.setText("Any Available Specialist");

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(13, 115, 119));
        jLabel9.setText("URGENCY");

        jCheckBox2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(13, 115, 119));
        jCheckBox2.setText("Routine");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jCheckBox3.setForeground(new java.awt.Color(13, 115, 119));
        jCheckBox3.setText("Urgent");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jCheckBox4.setForeground(new java.awt.Color(13, 115, 119));
        jCheckBox4.setText("Emergency");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(13, 115, 119));
        jLabel10.setText("REASON FOR REFERRAL");

        txtAreaReferral.setColumns(20);
        txtAreaReferral.setForeground(new java.awt.Color(13, 115, 119));
        txtAreaReferral.setRows(5);
        jScrollPane1.setViewportView(txtAreaReferral);

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(13, 115, 119));
        jLabel11.setText("CLINICAL SUMMARY FOR SPECIALIST ");

        txtAreaClinicalSummanry.setColumns(20);
        txtAreaClinicalSummanry.setForeground(new java.awt.Color(13, 115, 119));
        txtAreaClinicalSummanry.setRows(5);
        jScrollPane2.setViewportView(txtAreaClinicalSummanry);

        btnSubmitReferal.setBackground(new java.awt.Color(13, 115, 119));
        btnSubmitReferal.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnSubmitReferal.setForeground(new java.awt.Color(232, 244, 248));
        btnSubmitReferal.setText("SUBMIT REFERRAL");
        btnSubmitReferal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitReferalActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(13, 115, 119));
        btnCancel.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(232, 244, 248));
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ComboBoxSpecialityRequires, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(comboBoxPreferredSpec, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(comboBoxPreferredSpec2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(48, 48, 48)
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox2)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox3)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox4))
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(btnSubmitReferal)
                .addGap(105, 105, 105)
                .addComponent(btnCancel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ComboBoxSpecialityRequires, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(comboBoxPreferredSpec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxPreferredSpec2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmitReferal)
                    .addComponent(btnCancel))
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        updateUrgencyCheckboxes(jCheckBox3);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void btnSubmitReferalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitReferalActionPerformed
        String specialtyRequired = (String) ComboBoxSpecialityRequires.getSelectedItem();
        String referralReason = txtAreaReferral.getText().trim();
        String clinicalSummary = txtAreaClinicalSummanry.getText().trim();

        if (ValidationUtil.isEmpty(specialtyRequired)) {
            JOptionPane.showMessageDialog(this, "Please select specialty required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ValidationUtil.isEmpty(referralReason)) {
            JOptionPane.showMessageDialog(this, "Please enter reason for referral", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ValidationUtil.isEmpty(clinicalSummary)) {
            JOptionPane.showMessageDialog(this, "Please enter clinical summary", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get urgency
        String urgency = getSelectedUrgency();

        // Create full referral reason
        String fullReason = referralReason + "\n\nClinical Summary: " + clinicalSummary
                + "\n\nUrgency: " + urgency
                + "\n\nSpecialty: " + specialtyRequired;

        // Get specialist ID (if not "any available")
        Integer specialistID = null;
        if (!jCheckBox1.isSelected()) {
            // For now, set to null (in production, get from combobox selection)
            specialistID = null;
        }

        // Create referral
        SpecialistReferral referral = doctorService.referToSpecialist(diagnosisID, fullReason, specialistID);

        if (referral != null) {
            JOptionPane.showMessageDialog(this,
                    "Referral submitted successfully!\n\n"
                    + "Referral ID: R" + referral.getReferralID() + "\n"
                    + "Specialty: " + specialtyRequired + "\n"
                    + "Urgency: " + urgency,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            navigateBackToDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit referral", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSubmitReferalActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        navigateBackToDashboard();
    }//GEN-LAST:event_btnBackActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        updateUrgencyCheckboxes(jCheckBox2);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        updateUrgencyCheckboxes(jCheckBox4);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel referral? Unsaved data will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            navigateBackToDashboard();
        }
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxSpecialityRequires;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSubmitReferal;
    private javax.swing.JComboBox<String> comboBoxPreferredSpec;
    private javax.swing.JComboBox<String> comboBoxPreferredSpec2;
    private javax.swing.JTextField fieldDiagnosis;
    private javax.swing.JTextField fieldPatient;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtAreaClinicalSummanry;
    private javax.swing.JTextArea txtAreaReferral;
    // End of variables declaration//GEN-END:variables
}
