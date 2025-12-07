/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Forms;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;
import util.ValidationUtil;
import util.PasswordUtil;

/**
 *
 * @author Chinmay
 */
public class ForgotPasswordForm extends JFrame {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnResetPassword;
    private JButton btnCancel;
    private JLabel lblInstruction;

    private UserDAO userDAO;

    public ForgotPasswordForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("MediNexus - Reset Password");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Instruction
        lblInstruction = new JLabel("Enter your username and registered email to reset password");
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblInstruction);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Username field
        mainPanel.add(createFieldPanel("Username:", txtUsername = new JTextField(20)));

        // Email field
        mainPanel.add(createFieldPanel("Email:", txtEmail = new JTextField(20)));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // New password field
        mainPanel.add(createFieldPanel("New Password:", txtNewPassword = new JPasswordField(20)));

        // Confirm password field
        mainPanel.add(createFieldPanel("Confirm Password:", txtConfirmPassword = new JPasswordField(20)));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnResetPassword = new JButton("Reset Password");
        btnResetPassword.setPreferredSize(new Dimension(150, 35));
        btnResetPassword.addActionListener(e -> handlePasswordReset());

        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.addActionListener(e -> this.setVisible(false));

        buttonPanel.add(btnResetPassword);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    // Create field panel
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(400, 35));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));

        panel.add(label);
        panel.add(field);

        return panel;
    }

    // Handle password reset
    private void handlePasswordReset() {
        // Get input values
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // Validate username
        if (ValidationUtil.isEmpty(username)) {
            showError("Username is required");
            return;
        }

        // Validate email
        if (!ValidationUtil.isValidEmail(email)) {
            showError("Invalid email format");
            return;
        }

        // Check if user exists
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            showError("User not found");
            return;
        }

        // Verify email matches
        if (!email.equalsIgnoreCase(user.getEmail())) {
            showError("Email does not match our records");
            return;
        }

        // Validate new password
        if (ValidationUtil.isEmpty(newPassword)) {
            showError("New password is required");
            return;
        }

        if (newPassword.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Check passwords match
        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Update password
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        boolean success = userDAO.updatePassword(user.getUserID(), hashedPassword);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Password reset successful!\n\nYou can now login with your new password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Close form
            this.setVisible(false);

        } else {
            showError("Failed to reset password. Please try again.");
        }
    }

    // Show error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Test the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordForm().setVisible(true);
        });
    }
}
