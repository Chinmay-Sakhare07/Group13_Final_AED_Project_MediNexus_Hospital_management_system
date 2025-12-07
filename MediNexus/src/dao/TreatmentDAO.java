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
import model.Treatment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {
    
    public boolean createTreatment(Treatment treatment) {
        String sql = "INSERT INTO Treatments (DiagnosisID, TreatmentPlan, MedicationsPrescribed, " +
                     "Instructions, FollowUpDate, PrescribedBy, PrescribedDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, treatment.getDiagnosisID());
            ps.setString(2, treatment.getTreatmentPlan());
            ps.setString(3, treatment.getMedicationsPrescribed());
            ps.setString(4, treatment.getInstructions());
            
            if (treatment.getFollowUpDate() != null) {
                ps.setDate(5, Date.valueOf(treatment.getFollowUpDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            
            ps.setInt(6, treatment.getPrescribedBy());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    treatment.setTreatmentID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Treatment getTreatmentByID(int treatmentID) {
        String sql = "SELECT * FROM Treatments WHERE TreatmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, treatmentID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractTreatmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Treatment getTreatmentByDiagnosisID(int diagnosisID) {
        String sql = "SELECT * FROM Treatments WHERE DiagnosisID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, diagnosisID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractTreatmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Treatment> getTreatmentsByDoctorID(int doctorID) {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM Treatments WHERE PrescribedBy = ? ORDER BY PrescribedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                treatments.add(extractTreatmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return treatments;
    }
    
    public List<Treatment> getAllTreatments() {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM Treatments ORDER BY PrescribedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                treatments.add(extractTreatmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return treatments;
    }
    
    public boolean updateTreatment(Treatment treatment) {
        String sql = "UPDATE Treatments SET TreatmentPlan = ?, MedicationsPrescribed = ?, " +
                     "Instructions = ?, FollowUpDate = ? WHERE TreatmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, treatment.getTreatmentPlan());
            ps.setString(2, treatment.getMedicationsPrescribed());
            ps.setString(3, treatment.getInstructions());
            
            if (treatment.getFollowUpDate() != null) {
                ps.setDate(4, Date.valueOf(treatment.getFollowUpDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.setInt(5, treatment.getTreatmentID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteTreatment(int treatmentID) {
        String sql = "DELETE FROM Treatments WHERE TreatmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, treatmentID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Treatment extractTreatmentFromResultSet(ResultSet rs) throws SQLException {
        Treatment treatment = new Treatment();
        treatment.setTreatmentID(rs.getInt("TreatmentID"));
        treatment.setDiagnosisID(rs.getInt("DiagnosisID"));
        treatment.setTreatmentPlan(rs.getString("TreatmentPlan"));
        treatment.setMedicationsPrescribed(rs.getString("MedicationsPrescribed"));
        treatment.setInstructions(rs.getString("Instructions"));
        
        Date followUpDate = rs.getDate("FollowUpDate");
        if (followUpDate != null) {
            treatment.setFollowUpDate(followUpDate.toLocalDate());
        }
        
        treatment.setPrescribedBy(rs.getInt("PrescribedBy"));
        
        Timestamp prescribedTimestamp = rs.getTimestamp("PrescribedDate");
        if (prescribedTimestamp != null) {
            treatment.setPrescribedDate(prescribedTimestamp.toLocalDateTime());
        }
        
        return treatment;
    }
}
