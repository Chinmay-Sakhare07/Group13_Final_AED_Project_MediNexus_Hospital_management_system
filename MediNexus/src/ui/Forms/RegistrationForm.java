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
import java.awt.event.*;
import services.RegistrationService;
import util.ValidationUtil;
import dao.AuditLogDAO;
import model.AuditLog;
import java.time.LocalDate;

public class RegistrationForm extends JFrame {

    // Form fields
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDOB;
    private JComboBox<String> cmbGender;
    private JComboBox<String> cmbBloodGroup;
    private JTextField txtAddress;
    private JTextField txtEmergencyContact;
    private JTextField txtInsuranceID;
    private JButton btnRegister;
    private JButton btnCancel;

    private RegistrationService registrationService;

    public RegistrationForm() {
        registrationService = new RegistrationService();
        initComponents();
    }

    private void initComponents() {
        setTitle("MediNexus - Patient Registration");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel titleLabel = new JLabel("Patient Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Account Information Section
        mainPanel.add(createSectionLabel("Account Information"));
        mainPanel.add(createFieldPanel("Username:", txtUsername = new JTextField(20)));
        mainPanel.add(createFieldPanel("Password:", txtPassword = new JPasswordField(20)));
        mainPanel.add(createFieldPanel("Confirm Password:", txtConfirmPassword = new JPasswordField(20)));
        mainPanel.add(createFieldPanel("Email:", txtEmail = new JTextField(20)));
        mainPanel.add(createFieldPanel("Phone (10 digits):", txtPhone = new JTextField(20)));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Personal Information Section
        mainPanel.add(createSectionLabel("Personal Information"));
        mainPanel.add(createFieldPanel("First Name:", txtFirstName = new JTextField(20)));
        mainPanel.add(createFieldPanel("Last Name:", txtLastName = new JTextField(20)));
        mainPanel.add(createFieldPanel("Date of Birth (MM/DD/YYYY):", txtDOB = new JTextField(20)));

        // Gender dropdown
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        mainPanel.add(createFieldPanel("Gender:", cmbGender));

        // Blood group dropdown
        cmbBloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Unknown"});
        mainPanel.add(createFieldPanel("Blood Group:", cmbBloodGroup));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Additional Information Section
        mainPanel.add(createSectionLabel("Additional Information"));
        mainPanel.add(createFieldPanel("Address:", txtAddress = new JTextField(20)));
        mainPanel.add(createFieldPanel("Emergency Contact:", txtEmergencyContact = new JTextField(20)));
        mainPanel.add(createFieldPanel("Insurance ID:", txtInsuranceID = new JTextField(20)));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(120, 35));
        btnRegister.addActionListener(e -> handleRegistration());

        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(120, 35));
        btnCancel.addActionListener(e -> {
            this.setVisible(false);
        });

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    // Create section label
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Create field panel
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(450, 35));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(200, 25));

        panel.add(label);
        panel.add(field);

        return panel;
    }

    // Handle registration
    private void handleRegistration() {
        // Get all input values
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dobStr = txtDOB.getText().trim();
        String gender = (String) cmbGender.getSelectedItem();
        String bloodGroup = (String) cmbBloodGroup.getSelectedItem();
        String address = txtAddress.getText().trim();
        String emergencyContact = txtEmergencyContact.getText().trim();
        String insuranceID = txtInsuranceID.getText().trim();

        // Validate username
        if (ValidationUtil.isEmpty(username)) {
            showError("Username is required");
            return;
        }

        // Check if username exists
        if (!registrationService.isUsernameAvailable(username)) {
            showError("Username already taken");
            return;
        }

        // Validate password
        if (ValidationUtil.isEmpty(password)) {
            showError("Password is required");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Validate email
        if (!ValidationUtil.isValidEmail(email)) {
            showError("Invalid email format");
            return;
        }

        // Validate phone
        if (!ValidationUtil.isValidPhone(phone)) {
            showError("Phone must be 10 digits");
            return;
        }

        // Validate names
        if (ValidationUtil.isEmpty(firstName) || ValidationUtil.isEmpty(lastName)) {
            showError("First name and last name are required");
            return;
        }

        // Parse date of birth
        LocalDate dob;
        try {
            String[] parts = dobStr.split("/");
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            dob = LocalDate.of(year, month, day);
        } catch (Exception e) {
            showError("Invalid date format. Use MM/DD/YYYY");
            return;
        }

        // Validate age
        if (!ValidationUtil.isValidAge(dob)) {
            showError("Invalid date of birth");
            return;
        }

        // Use emergency contact from phone if not provided
        if (ValidationUtil.isEmpty(emergencyContact)) {
            emergencyContact = phone;
        }

        // Register patient
        boolean success = registrationService.registerPatient(
                username, password, email, phone,
                firstName, lastName, dob,
                gender, bloodGroup
        );

        if (success) {
            // Create audit log
            createAuditLog(0, "USER_REGISTERED", "USER", null, "New patient registered: " + username, "127.0.0.1");

            JOptionPane.showMessageDialog(this,
                    "Registration successful!\n\n"
                    + "Username: " + username + "\n"
                    + "Email: " + email + "\n\n"
                    + "You can now login with your credentials.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Close registration form
            this.setVisible(false);

        } else {
            showError("Registration failed. Please try again.");
        }
    }

    // Show error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Create audit log
    private void createAuditLog(int userID, String action, String entityType,
            Integer entityID, String details, String ip) {
        try {
            AuditLogDAO dao = new AuditLogDAO();
            AuditLog log = new AuditLog();
            log.setUserID(userID);
            log.setAction(action);
            log.setEntityType(entityType);
            log.setEntityID(entityID);
            log.setDetails(details);
            log.setIpAddress(ip);
            dao.createAuditLog(log);
        } catch (Exception e) {
            System.err.println("Failed to create audit log");
        }
    }

    // Test the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrationForm().setVisible(true);
        });
    }
}
