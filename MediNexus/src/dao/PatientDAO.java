/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Shreya
 */

import config.DatabaseConnection;
import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    // CREATE - Add new patient
    public boolean createPatient(Patient patient) {
        String sql = "INSERT INTO Patients (UserID, FirstName, LastName, DateOfBirth, Gender, BloodGroup, Address, EmergencyContact, InsuranceID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, patient.getUserID());
            ps.setString(2, patient.getFirstName());
            ps.setString(3, patient.getLastName());
            ps.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(5, patient.getGender());
            ps.setString(6, patient.getBloodGroup());
            ps.setString(7, patient.getAddress());
            ps.setString(8, patient.getEmergencyContact());
            ps.setString(9, patient.getInsuranceID());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    patient.setPatientID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // READ - Get patient by ID
    public Patient getPatientByID(int patientID) {
        String sql = "SELECT * FROM Patients WHERE PatientID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractPatientFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get patient by UserID
    public Patient getPatientByUserID(int userID) {
        String sql = "SELECT * FROM Patients WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractPatientFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get all patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients ORDER BY LastName, FirstName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patients;
    }
    
    // READ - Search patients by name
    public List<Patient> searchPatientsByName(String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients WHERE FirstName LIKE ? OR LastName LIKE ? ORDER BY LastName, FirstName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patients;
    }
    
    // UPDATE - Update patient information
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE Patients SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, " +
                     "BloodGroup = ?, Address = ?, EmergencyContact = ?, InsuranceID = ? WHERE PatientID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getBloodGroup());
            ps.setString(6, patient.getAddress());
            ps.setString(7, patient.getEmergencyContact());
            ps.setString(8, patient.getInsuranceID());
            ps.setInt(9, patient.getPatientID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // DELETE - Delete patient
    public boolean deletePatient(int patientID) {
        String sql = "DELETE FROM Patients WHERE PatientID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, patientID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper method to extract Patient from ResultSet
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientID(rs.getInt("PatientID"));
        patient.setUserID(rs.getInt("UserID"));
        patient.setFirstName(rs.getString("FirstName"));
        patient.setLastName(rs.getString("LastName"));
        
        Date dob = rs.getDate("DateOfBirth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }
        
        patient.setGender(rs.getString("Gender"));
        patient.setBloodGroup(rs.getString("BloodGroup"));
        patient.setAddress(rs.getString("Address"));
        patient.setEmergencyContact(rs.getString("EmergencyContact"));
        patient.setInsuranceID(rs.getString("InsuranceID"));
        
        return patient;
    }
}