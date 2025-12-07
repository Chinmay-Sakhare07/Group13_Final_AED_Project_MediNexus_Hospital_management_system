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
import model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {
    
    // CREATE - Add new complaint
    public boolean createComplaint(Complaint complaint) {
        String sql = "INSERT INTO Complaints (PatientID, Category, Description, Status, Priority, CreatedDate, LastUpdated) " +
                     "VALUES (?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, complaint.getPatientID());
            ps.setString(2, complaint.getCategory());
            ps.setString(3, complaint.getDescription());
            ps.setString(4, complaint.getStatus());
            ps.setString(5, complaint.getPriority());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    complaint.setComplaintID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // READ - Get complaint by ID
    public Complaint getComplaintByID(int complaintID) {
        String sql = "SELECT * FROM Complaints WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, complaintID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractComplaintFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get complaints by patient ID
    public List<Complaint> getComplaintsByPatientID(int patientID) {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM Complaints WHERE PatientID = ? ORDER BY CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // READ - Get complaints by doctor ID
    public List<Complaint> getComplaintsByDoctorID(int doctorID) {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM Complaints WHERE AssignedDoctorID = ? ORDER BY Priority DESC, CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // READ - Get complaints by status
    public List<Complaint> getComplaintsByStatus(String status) {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM Complaints WHERE Status = ? ORDER BY CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // READ - Get unassigned complaints (for doctor to pick up)
    public List<Complaint> getUnassignedComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM Complaints WHERE AssignedDoctorID IS NULL ORDER BY Priority DESC, CreatedDate ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // READ - Get all complaints
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM Complaints ORDER BY CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                complaints.add(extractComplaintFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return complaints;
    }
    
    // UPDATE - Assign complaint to doctor
    public boolean assignComplaintToDoctor(int complaintID, int doctorID) {
        String sql = "UPDATE Complaints SET AssignedDoctorID = ?, Status = 'ASSIGNED', LastUpdated = GETDATE() WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ps.setInt(2, complaintID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // UPDATE - Update complaint status
    public boolean updateComplaintStatus(int complaintID, String status) {
        String sql = "UPDATE Complaints SET Status = ?, LastUpdated = GETDATE() WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, complaintID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // UPDATE - Update entire complaint
    public boolean updateComplaint(Complaint complaint) {
        String sql = "UPDATE Complaints SET Category = ?, Description = ?, Status = ?, Priority = ?, " +
                     "AssignedDoctorID = ?, LastUpdated = GETDATE() WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, complaint.getCategory());
            ps.setString(2, complaint.getDescription());
            ps.setString(3, complaint.getStatus());
            ps.setString(4, complaint.getPriority());
            
            if (complaint.getAssignedDoctorID() != null) {
                ps.setInt(5, complaint.getAssignedDoctorID());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            
            ps.setInt(6, complaint.getComplaintID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // DELETE - Delete complaint
    public boolean deleteComplaint(int complaintID) {
        String sql = "DELETE FROM Complaints WHERE ComplaintID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, complaintID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper method to extract Complaint from ResultSet
    private Complaint extractComplaintFromResultSet(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setComplaintID(rs.getInt("ComplaintID"));
        complaint.setPatientID(rs.getInt("PatientID"));
        complaint.setCategory(rs.getString("Category"));
        complaint.setDescription(rs.getString("Description"));
        complaint.setStatus(rs.getString("Status"));
        complaint.setPriority(rs.getString("Priority"));
        
        int assignedDoctorID = rs.getInt("AssignedDoctorID");
        if (!rs.wasNull()) {
            complaint.setAssignedDoctorID(assignedDoctorID);
        }
        
        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            complaint.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        Timestamp updatedTimestamp = rs.getTimestamp("LastUpdated");
        if (updatedTimestamp != null) {
            complaint.setLastUpdated(updatedTimestamp.toLocalDateTime());
        }
        
        return complaint;
    }
}
