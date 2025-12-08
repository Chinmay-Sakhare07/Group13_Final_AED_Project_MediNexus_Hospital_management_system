/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Forms;

/**
 *
 * @author Chinmay
 */
import javax.swing.*;
import java.awt.*;
import dao.*;
import model.*;
import services.*;
import session.UserSession;
import util.ValidationUtil;

public class RecordVitalsForm extends JFrame {

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(13, 115, 119); // #0D7377
    private final Color LIGHT_BG = new Color(232, 244, 248); // #E8F4F8
    private final Color WHITE = Color.WHITE;

    // Form fields
    private JTextField fieldComplaintID;
    private JTextField fieldPatientName;
    private JTextField fieldBPSystolic;
    private JTextField fieldBPDiastolic;
    private JTextField fieldHeartRate;
    private JTextField fieldTemperature;
    private JTextField fieldRespiratoryRate;
    private JTextField fieldOxygenSaturation;
    private JTextField fieldWeight;
    private JButton btnSaveVitals;
    private JButton btnCancel;

    // Services and DAOs
    private VitalSignsService vitalSignsService;
    private ComplaintDAO complaintDAO;
    private PatientDAO patientDAO;

    // Data
    private int complaintID;
    private int patientID;

    public RecordVitalsForm(int complaintID, int patientID) {
        this.complaintID = complaintID;
        this.patientID = patientID;

        this.vitalSignsService = new VitalSignsService();
        this.complaintDAO = new ComplaintDAO();
        this.patientDAO = new PatientDAO();

        initComponents();
        loadComplaintInfo();
    }

    private void initComponents() {
        setTitle("MediNexus - Record Vital Signs");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(700, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));

        JLabel titleLabel = new JLabel("MEDINEXUS");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(LIGHT_BG);

        JLabel subtitleLabel = new JLabel("Record Vital Signs");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_BG);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(LIGHT_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Patient info section
        JPanel patientInfoPanel = new JPanel();
        patientInfoPanel.setBackground(WHITE);
        patientInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "PATIENT INFORMATION",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 13),
                PRIMARY_COLOR
        ));
        patientInfoPanel.setLayout(new GridLayout(2, 2, 20, 10));
        patientInfoPanel.setMaximumSize(new Dimension(600, 100));
        ((javax.swing.border.TitledBorder) patientInfoPanel.getBorder()).setTitleColor(PRIMARY_COLOR);

        patientInfoPanel.add(createStyledLabel("Complaint ID:"));
        fieldComplaintID = createStyledTextField(false);
        patientInfoPanel.add(fieldComplaintID);

        patientInfoPanel.add(createStyledLabel("Patient Name:"));
        fieldPatientName = createStyledTextField(false);
        patientInfoPanel.add(fieldPatientName);

        formPanel.add(patientInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Vital signs section
        JPanel vitalsPanel = new JPanel();
        vitalsPanel.setBackground(WHITE);
        vitalsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "VITAL SIGNS",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 13),
                PRIMARY_COLOR
        ));
        vitalsPanel.setLayout(new GridLayout(8, 2, 20, 15));
        vitalsPanel.setMaximumSize(new Dimension(600, 400));

        // Blood Pressure
        vitalsPanel.add(createStyledLabel("Blood Pressure (Systolic):"));
        fieldBPSystolic = createStyledTextField(true);
        vitalsPanel.add(fieldBPSystolic);

        vitalsPanel.add(createStyledLabel("Blood Pressure (Diastolic):"));
        fieldBPDiastolic = createStyledTextField(true);
        vitalsPanel.add(fieldBPDiastolic);

        // Heart Rate
        vitalsPanel.add(createStyledLabel("Heart Rate (bpm):"));
        fieldHeartRate = createStyledTextField(true);
        vitalsPanel.add(fieldHeartRate);

        // Temperature
        vitalsPanel.add(createStyledLabel("Temperature (°F):"));
        fieldTemperature = createStyledTextField(true);
        vitalsPanel.add(fieldTemperature);

        // Respiratory Rate
        vitalsPanel.add(createStyledLabel("Respiratory Rate (/min):"));
        fieldRespiratoryRate = createStyledTextField(true);
        vitalsPanel.add(fieldRespiratoryRate);

        // Oxygen Saturation
        vitalsPanel.add(createStyledLabel("Oxygen Saturation (%):"));
        fieldOxygenSaturation = createStyledTextField(true);
        vitalsPanel.add(fieldOxygenSaturation);

        // Weight
        vitalsPanel.add(createStyledLabel("Weight (kg):"));
        fieldWeight = createStyledTextField(true);
        vitalsPanel.add(fieldWeight);

        formPanel.add(vitalsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(LIGHT_BG);

        btnSaveVitals = new JButton("SAVE VITALS");
        btnSaveVitals.setPreferredSize(new Dimension(140, 40));
        btnSaveVitals.setBackground(PRIMARY_COLOR);
        btnSaveVitals.setForeground(LIGHT_BG);
        btnSaveVitals.setFont(new Font("Arial", Font.BOLD, 13));
        btnSaveVitals.setFocusPainted(false);
        btnSaveVitals.addActionListener(e -> handleSaveVitals());

        btnCancel = new JButton("CANCEL");
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(PRIMARY_COLOR);
        btnCancel.setForeground(LIGHT_BG);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> this.setVisible(false));

        buttonPanel.add(btnSaveVitals);
        buttonPanel.add(btnCancel);

        formPanel.add(buttonPanel);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBackground(LIGHT_BG);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Create styled label
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }

    // Create styled text field
    private JTextField createStyledTextField(boolean editable) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(LIGHT_BG);
        field.setForeground(PRIMARY_COLOR);
        field.setEditable(editable);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    // Load complaint information
    private void loadComplaintInfo() {
        try {
            fieldComplaintID.setText("C" + complaintID);

            Patient patient = patientDAO.getPatientByID(patientID);

            if (patient != null) {
                fieldPatientName.setText(patient.getFullName());
            }

        } catch (Exception e) {
            System.err.println("Error loading complaint info: " + e.getMessage());
        }
    }

    // Handle save vitals
    private void handleSaveVitals() {
        try {
            // Validate all fields
            String bpSystolicStr = fieldBPSystolic.getText().trim();
            String bpDiastolicStr = fieldBPDiastolic.getText().trim();
            String heartRateStr = fieldHeartRate.getText().trim();
            String temperatureStr = fieldTemperature.getText().trim();
            String respiratoryRateStr = fieldRespiratoryRate.getText().trim();
            String oxygenSaturationStr = fieldOxygenSaturation.getText().trim();
            String weightStr = fieldWeight.getText().trim();

            // Parse values (all fields are optional, so check before parsing)
            Integer bpSystolic = null;
            Integer bpDiastolic = null;
            Integer heartRate = null;
            Double temperature = null;
            Integer respiratoryRate = null;
            Integer oxygenSaturation = null;
            Double weight = null;

            // Parse BP Systolic
            if (!ValidationUtil.isEmpty(bpSystolicStr)) {
                if (!ValidationUtil.isValidInteger(bpSystolicStr)) {
                    showError("BP Systolic must be a valid number");
                    return;
                }
                bpSystolic = Integer.parseInt(bpSystolicStr);
                if (bpSystolic < 50 || bpSystolic > 250) {
                    showError("BP Systolic must be between 50-250");
                    return;
                }
            }

            // Parse BP Diastolic
            if (!ValidationUtil.isEmpty(bpDiastolicStr)) {
                if (!ValidationUtil.isValidInteger(bpDiastolicStr)) {
                    showError("BP Diastolic must be a valid number");
                    return;
                }
                bpDiastolic = Integer.parseInt(bpDiastolicStr);
                if (bpDiastolic < 30 || bpDiastolic > 150) {
                    showError("BP Diastolic must be between 30-150");
                    return;
                }
            }

            // Parse Heart Rate
            if (!ValidationUtil.isEmpty(heartRateStr)) {
                if (!ValidationUtil.isValidInteger(heartRateStr)) {
                    showError("Heart Rate must be a valid number");
                    return;
                }
                heartRate = Integer.parseInt(heartRateStr);
                if (heartRate < 30 || heartRate > 200) {
                    showError("Heart Rate must be between 30-200");
                    return;
                }
            }

            // Parse Temperature
            if (!ValidationUtil.isEmpty(temperatureStr)) {
                if (!ValidationUtil.isPositiveNumber(temperatureStr)) {
                    showError("Temperature must be a valid number");
                    return;
                }
                temperature = Double.parseDouble(temperatureStr);
                if (temperature < 90 || temperature > 110) {
                    showError("Temperature must be between 90-110°F");
                    return;
                }
            }

            // Parse Respiratory Rate
            if (!ValidationUtil.isEmpty(respiratoryRateStr)) {
                if (!ValidationUtil.isValidInteger(respiratoryRateStr)) {
                    showError("Respiratory Rate must be a valid number");
                    return;
                }
                respiratoryRate = Integer.parseInt(respiratoryRateStr);
                if (respiratoryRate < 8 || respiratoryRate > 40) {
                    showError("Respiratory Rate must be between 8-40");
                    return;
                }
            }

            // Parse Oxygen Saturation
            if (!ValidationUtil.isEmpty(oxygenSaturationStr)) {
                if (!ValidationUtil.isValidInteger(oxygenSaturationStr)) {
                    showError("Oxygen Saturation must be a valid number");
                    return;
                }
                oxygenSaturation = Integer.parseInt(oxygenSaturationStr);
                if (oxygenSaturation < 70 || oxygenSaturation > 100) {
                    showError("Oxygen Saturation must be between 70-100%");
                    return;
                }
            }

            // Parse Weight
            if (!ValidationUtil.isEmpty(weightStr)) {
                if (!ValidationUtil.isPositiveNumber(weightStr)) {
                    showError("Weight must be a valid number");
                    return;
                }
                weight = Double.parseDouble(weightStr);
                if (weight < 1 || weight > 300) {
                    showError("Weight must be between 1-300 kg");
                    return;
                }
            }

            // At least one vital sign must be entered
            if (bpSystolic == null && bpDiastolic == null && heartRate == null
                    && temperature == null && respiratoryRate == null && oxygenSaturation == null && weight == null) {
                showError("Please enter at least one vital sign");
                return;
            }

            // Record vitals using service
            VitalSigns vitals = vitalSignsService.recordVitalSigns(
                    complaintID,
                    patientID,
                    bpSystolic,
                    bpDiastolic,
                    heartRate,
                    temperature,
                    respiratoryRate,
                    oxygenSaturation,
                    weight
            );

            if (vitals != null) {
                JOptionPane.showMessageDialog(this,
                        "Vital signs recorded successfully!\n\n"
                        + "Vital Sign ID: V" + vitals.getVitalSignID() + "\n"
                        + "BP: " + vitals.getBloodPressure() + "\n"
                        + "HR: " + (heartRate != null ? heartRate + " bpm" : "N/A") + "\n"
                        + "Temp: " + (temperature != null ? temperature + "°F" : "N/A") + "\n"
                        + "SpO2: " + (oxygenSaturation != null ? oxygenSaturation + "%" : "N/A"),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                this.setVisible(false);
            } else {
                showError("Failed to record vital signs");
            }

        } catch (Exception e) {
            System.err.println("Error saving vitals: " + e.getMessage());
            e.printStackTrace();
            showError("Error: " + e.getMessage());
        }
    }

    // Show error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    // Test the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test with complaint ID 1 and patient ID 1
            new RecordVitalsForm(1, 1).setVisible(true);
        });
    }
}
