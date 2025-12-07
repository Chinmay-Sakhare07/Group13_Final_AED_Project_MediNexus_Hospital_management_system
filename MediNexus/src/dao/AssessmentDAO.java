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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Assessment;

public class AssessmentDAO {
    
    public boolean createAssessment(Assessment assessment) {
        String sql = "INSERT INTO Assessments (ComplaintID, DoctorID, Symptoms, PhysicalExamination, " +
                     "PreliminaryNotes, AssessmentDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, assessment.getComplaintID());
            ps.setInt(2, assessment.getDoctorID());
            ps.setString(3, assessment.getSymptoms());
            ps.setString(4, assessment.getPhysicalExamination());
            ps.setString(5, assessment.getPreliminaryNotes());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    assessment.setAssessmentID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Assessment getAssessmentByID(int assessmentID) {
        String sql = "SELECT * FROM Assessments WHERE AssessmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, assessmentID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAssessmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Assessment getAssessmentByComplaintID(int complaintID) {
        String sql = "SELECT * FROM Assessments WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, complaintID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAssessmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Assessment> getAssessmentsByDoctorID(int doctorID) {
        List<Assessment> assessments = new ArrayList<>();
        String sql = "SELECT * FROM Assessments WHERE DoctorID = ? ORDER BY AssessmentDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                assessments.add(extractAssessmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assessments;
    }
    
    public boolean updateAssessment(Assessment assessment) {
        String sql = "UPDATE Assessments SET Symptoms = ?, PhysicalExamination = ?, PreliminaryNotes = ? " +
                     "WHERE AssessmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, assessment.getSymptoms());
            ps.setString(2, assessment.getPhysicalExamination());
            ps.setString(3, assessment.getPreliminaryNotes());
            ps.setInt(4, assessment.getAssessmentID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteAssessment(int assessmentID) {
        String sql = "DELETE FROM Assessments WHERE AssessmentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, assessmentID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Assessment extractAssessmentFromResultSet(ResultSet rs) throws SQLException {
        Assessment assessment = new Assessment();
        assessment.setAssessmentID(rs.getInt("AssessmentID"));
        assessment.setComplaintID(rs.getInt("ComplaintID"));
        assessment.setDoctorID(rs.getInt("DoctorID"));
        assessment.setSymptoms(rs.getString("Symptoms"));
        assessment.setPhysicalExamination(rs.getString("PhysicalExamination"));
        assessment.setPreliminaryNotes(rs.getString("PreliminaryNotes"));
        
        Timestamp assessmentTimestamp = rs.getTimestamp("AssessmentDate");
        if (assessmentTimestamp != null) {
            assessment.setAssessmentDate(assessmentTimestamp.toLocalDateTime());
        }
        
        return assessment;
    }
}
