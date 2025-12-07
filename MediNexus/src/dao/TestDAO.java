/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Chinmay
 */

import model.*;
import java.util.List;

public class TestDAO {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TESTING DAO OPERATIONS");
        System.out.println("========================================\n");
        
        // Test UserDAO
        testUserDAO();
        
        // Test PatientDAO
        testPatientDAO();
        
        // Test ComplaintDAO
        testComplaintDAO();
        
        System.out.println("\n========================================");
        System.out.println("  ALL DAO TESTS COMPLETED!");
        System.out.println("========================================");
    }
    
    private static void testUserDAO() {
        System.out.println("TEST 1: UserDAO");
        UserDAO userDAO = new UserDAO();
        
        // Get user by username
        User user = userDAO.getUserByUsername("testdoctor");
        if (user != null) {
            System.out.println("  Found user: " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("  User not found");
        }
    }
    
    private static void testPatientDAO() {
        System.out.println("\nTEST 2: PatientDAO");
        PatientDAO patientDAO = new PatientDAO();
        
        // Get first patient
        List<Patient> patients = patientDAO.getAllPatients();
        if (!patients.isEmpty()) {
            Patient patient = patients.get(0);
            System.out.println("  Found patient: " + patient.getFullName() + " (Age: " + patient.getAge() + ")");
        } else {
            System.out.println("  No patients found");
        }
    }
    
    private static void testComplaintDAO() {
        System.out.println("\nTEST 3: ComplaintDAO");
        ComplaintDAO complaintDAO = new ComplaintDAO();
        
        // Get all complaints
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        System.out.println("  Total complaints in system: " + complaints.size());
    }
}
