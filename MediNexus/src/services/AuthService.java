/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Chinmay
 */

import dao.UserDAO;
import dao.PatientDAO;
import dao.EmployeeDAO;
import model.User;
import model.Patient;
import model.Employee;
import session.UserSession;
import util.PasswordUtil;

/**
 * Authentication service - handles login/logout
 */
public class AuthService {
    
    private UserDAO userDAO;
    private PatientDAO patientDAO;
    private EmployeeDAO employeeDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
        this.patientDAO = new PatientDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Login user
     * @param username Username
     * @param password Plain text password
     * @return User object if login successful, null otherwise
     */
    public User login(String username, String password) {
        // Get user from database
        User user = userDAO.getUserByUsername(username);
        
        // Check if user exists
        if (user == null) {
            System.out.println("User not found: " + username);
            return null;
        }
        
        // Check if user is active
        if (!user.isActive()) {
            System.out.println("User account is inactive: " + username);
            return null;
        }
        
        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            System.out.println("Invalid password for user: " + username);
            return null;
        }
        
        // Update last login time
        userDAO.updateLastLogin(user.getUserID());
        
        // Set user in session
        UserSession.getInstance().setCurrentUser(user);
        
        // Load additional data based on role
        if ("PATIENT".equals(user.getRole())) {
            Patient patient = patientDAO.getPatientByUserID(user.getUserID());
            UserSession.getInstance().setCurrentPatient(patient);
        } else if ("DOCTOR".equals(user.getRole()) || 
                   "SPECIALIST".equals(user.getRole()) || 
                   "PHARMACIST".equals(user.getRole())) {
            Employee employee = employeeDAO.getEmployeeByUserID(user.getUserID());
            UserSession.getInstance().setCurrentEmployee(employee);
        }
        
        System.out.println("Login successful: " + username + " (" + user.getRole() + ")");
        return user;
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getUsername());
        }
        UserSession.getInstance().clearSession();
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return UserSession.getInstance().isLoggedIn();
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return UserSession.getInstance().getCurrentUser();
    }
    
    /**
     * Test authentication
     */
    public static void main(String[] args) {
        System.out.println("=== Authentication Service Test ===\n");
        
        AuthService authService = new AuthService();
        
        // Test login with correct credentials
        System.out.println("TEST 1: Login with correct credentials");
        User user = authService.login("patient1", "password123");
        
        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("  User: " + user.getUsername());
            System.out.println("  Role: " + user.getRole());
            System.out.println("  Email: " + user.getEmail());
            
            // Display session info
            UserSession.getInstance().displaySessionInfo();
            
            // Logout
            authService.logout();
        } else {
            System.out.println("Login failed!");
        }
        
        System.out.println("\nTEST 2: Login with wrong password");
        user = authService.login("patient1", "wrongpassword");
        if (user == null) {
            System.out.println("Correctly rejected wrong password");
        }
        
        System.out.println("\nTEST 3: Login with non-existent user");
        user = authService.login("nonexistent", "password123");
        if (user == null) {
            System.out.println("Correctly rejected non-existent user");
        }
        
        System.out.println("\nAll authentication tests completed!");
    }
}