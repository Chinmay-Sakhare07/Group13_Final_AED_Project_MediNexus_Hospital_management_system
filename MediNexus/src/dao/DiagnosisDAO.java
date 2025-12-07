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
import model.Diagnosis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDAO {
    
    public boolean createDiagnosis(Diagnosis diagnosis) {
        String sql = "INSERT INTO Diagnoses (AssessmentID, DiagnosisCode, DiagnosisName, Description, " +
                     "Severity, DiagnosedBy, DiagnosisDate) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, diagnosis.getAssessmentID());
            ps.setString(2, diagnosis.getDiagnosisCode());
            ps.setString(3, diagnosis.getDiagnosisName());
            ps.setString(4, diagnosis.getDescription());
            ps.setString(5, diagnosis.getSeverity());
            ps.setInt(6, diagnosis.getDiagnosedBy());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    diagnosis.setDiagnosisID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Diagnosis getDiagnosisByID(int diagnosisID) {
        String sql = "SELECT * FROM Diagnoses WHERE DiagnosisID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, diagnosisID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractDiagnosisFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Diagnosis getDiagnosisByAssessmentID(int assessmentID) {
        String sql = "SELECT * FROM Diagnoses WHERE AssessmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, assessmentID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractDiagnosisFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Diagnosis> getDiagnosesByDoctorID(int doctorID) {
        List<Diagnosis> diagnoses = new ArrayList<>();
        String sql = "SELECT * FROM Diagnoses WHERE DiagnosedBy = ? ORDER BY DiagnosisDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                diagnoses.add(extractDiagnosisFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return diagnoses;
    }
    
    public List<Diagnosis> getAllDiagnoses() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        String sql = "SELECT * FROM Diagnoses ORDER BY DiagnosisDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                diagnoses.add(extractDiagnosisFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return diagnoses;
    }
    
    public boolean updateDiagnosis(Diagnosis diagnosis) {
        String sql = "UPDATE Diagnoses SET DiagnosisCode = ?, DiagnosisName = ?, Description = ?, " +
                     "Severity = ? WHERE DiagnosisID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, diagnosis.getDiagnosisCode());
            ps.setString(2, diagnosis.getDiagnosisName());
            ps.setString(3, diagnosis.getDescription());
            ps.setString(4, diagnosis.getSeverity());
            ps.setInt(5, diagnosis.getDiagnosisID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteDiagnosis(int diagnosisID) {
        String sql = "DELETE FROM Diagnoses WHERE DiagnosisID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, diagnosisID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Diagnosis extractDiagnosisFromResultSet(ResultSet rs) throws SQLException {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisID(rs.getInt("DiagnosisID"));
        diagnosis.setAssessmentID(rs.getInt("AssessmentID"));
        diagnosis.setDiagnosisCode(rs.getString("DiagnosisCode"));
        diagnosis.setDiagnosisName(rs.getString("DiagnosisName"));
        diagnosis.setDescription(rs.getString("Description"));
        diagnosis.setSeverity(rs.getString("Severity"));
        diagnosis.setDiagnosedBy(rs.getInt("DiagnosedBy"));
        
        Timestamp diagnosisTimestamp = rs.getTimestamp("DiagnosisDate");
        if (diagnosisTimestamp != null) {
            diagnosis.setDiagnosisDate(diagnosisTimestamp.toLocalDateTime());
        }
        
        return diagnosis;
    }
}