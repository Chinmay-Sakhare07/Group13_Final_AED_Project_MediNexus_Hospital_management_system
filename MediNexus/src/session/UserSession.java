/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

/**
 *
 * @author Chinmay
 */

import model.User;
import model.Patient;
import model.Employee;

/**
 * Singleton class to manage logged-in user session
 */
public class UserSession {
    
    private static UserSession instance;
    
    private User currentUser;
    private Patient currentPatient;
    private Employee currentEmployee;
    
    // Private constructor (Singleton pattern)
    private UserSession() {}
    
    /**
     * Get singleton instance
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * Set logged-in user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Get logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current patient (if user is a patient)
     */
    public void setCurrentPatient(Patient patient) {
        this.currentPatient = patient;
    }
    
    /**
     * Get current patient
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }
    
    /**
     * Set current employee (if user is an employee)
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }
    
    /**
     * Get current employee
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Get user role
     */
    public String getUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }
    
    /**
     * Check if user has specific role
     */
    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().equals(role);
    }
    
    /**
     * Clear session
     */
    public void clearSession() {
        currentUser = null;
        currentPatient = null;
        currentEmployee = null;
        System.out.println("Session cleared - User logged out");
    }
    
    /**
     * Display session info
     */
    public void displaySessionInfo() {
        if (currentUser != null) {
            System.out.println("========================================");
            System.out.println("CURRENT SESSION INFO");
            System.out.println("========================================");
            System.out.println("User ID: " + currentUser.getUserID());
            System.out.println("Username: " + currentUser.getUsername());
            System.out.println("Role: " + currentUser.getRole());
            System.out.println("Email: " + currentUser.getEmail());
            System.out.println("Status: LOGGED IN");
            System.out.println("========================================");
        } else {
            System.out.println("No user logged in.");
        }
    }
}