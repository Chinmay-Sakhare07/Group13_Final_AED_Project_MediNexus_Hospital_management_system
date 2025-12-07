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
import model.InsuranceClaim;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsuranceClaimDAO {
    
    public boolean createClaim(InsuranceClaim claim) {
        String sql = "INSERT INTO InsuranceClaims (PatientID, TreatmentID, ClaimAmount, ClaimReason, " +
                     "Status, SubmittedDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, claim.getPatientID());
            ps.setInt(2, claim.getTreatmentID());
            ps.setDouble(3, claim.getClaimAmount());
            ps.setString(4, claim.getClaimReason());
            ps.setString(5, claim.getStatus());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    claim.setClaimID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public InsuranceClaim getClaimByID(int claimID) {
        String sql = "SELECT * FROM InsuranceClaims WHERE ClaimID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, claimID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractClaimFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<InsuranceClaim> getClaimsByPatientID(int patientID) {
        List<InsuranceClaim> claims = new ArrayList<>();
        String sql = "SELECT * FROM InsuranceClaims WHERE PatientID = ? ORDER BY SubmittedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                claims.add(extractClaimFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return claims;
    }
    
    public List<InsuranceClaim> getClaimsByStatus(String status) {
        List<InsuranceClaim> claims = new ArrayList<>();
        String sql = "SELECT * FROM InsuranceClaims WHERE Status = ? ORDER BY SubmittedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                claims.add(extractClaimFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return claims;
    }
    
    public List<InsuranceClaim> getClaimsByProcessorID(int processorID) {
        List<InsuranceClaim> claims = new ArrayList<>();
        String sql = "SELECT * FROM InsuranceClaims WHERE ProcessedBy = ? ORDER BY ProcessedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, processorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                claims.add(extractClaimFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return claims;
    }
    
    public List<InsuranceClaim> getAllClaims() {
        List<InsuranceClaim> claims = new ArrayList<>();
        String sql = "SELECT * FROM InsuranceClaims ORDER BY SubmittedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                claims.add(extractClaimFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return claims;
    }
    
    public boolean processClaim(int claimID, String status, Double approvedAmount, 
                                String rejectionReason, int processorID) {
        String sql = "UPDATE InsuranceClaims SET Status = ?, ApprovedAmount = ?, RejectionReason = ?, " +
                     "ProcessedBy = ?, ProcessedDate = GETDATE() WHERE ClaimID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            
            if (approvedAmount != null) {
                ps.setDouble(2, approvedAmount);
            } else {
                ps.setNull(2, Types.DOUBLE);
            }
            
            ps.setString(3, rejectionReason);
            ps.setInt(4, processorID);
            ps.setInt(5, claimID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateClaim(InsuranceClaim claim) {
        String sql = "UPDATE InsuranceClaims SET ClaimAmount = ?, ApprovedAmount = ?, Status = ?, " +
                     "ClaimReason = ?, RejectionReason = ? WHERE ClaimID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, claim.getClaimAmount());
            
            if (claim.getApprovedAmount() != null) {
                ps.setDouble(2, claim.getApprovedAmount());
            } else {
                ps.setNull(2, Types.DOUBLE);
            }
            
            ps.setString(3, claim.getStatus());
            ps.setString(4, claim.getClaimReason());
            ps.setString(5, claim.getRejectionReason());
            ps.setInt(6, claim.getClaimID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteClaim(int claimID) {
        String sql = "DELETE FROM InsuranceClaims WHERE ClaimID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, claimID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private InsuranceClaim extractClaimFromResultSet(ResultSet rs) throws SQLException {
        InsuranceClaim claim = new InsuranceClaim();
        claim.setClaimID(rs.getInt("ClaimID"));
        claim.setPatientID(rs.getInt("PatientID"));
        claim.setTreatmentID(rs.getInt("TreatmentID"));
        claim.setClaimAmount(rs.getDouble("ClaimAmount"));
        
        double approvedAmount = rs.getDouble("ApprovedAmount");
        if (!rs.wasNull()) {
            claim.setApprovedAmount(approvedAmount);
        }
        
        claim.setStatus(rs.getString("Status"));
        claim.setClaimReason(rs.getString("ClaimReason"));
        claim.setRejectionReason(rs.getString("RejectionReason"));
        
        int processedBy = rs.getInt("ProcessedBy");
        if (!rs.wasNull()) {
            claim.setProcessedBy(processedBy);
        }
        
        Timestamp submittedTimestamp = rs.getTimestamp("SubmittedDate");
        if (submittedTimestamp != null) {
            claim.setSubmittedDate(submittedTimestamp.toLocalDateTime());
        }
        
        Timestamp processedTimestamp = rs.getTimestamp("ProcessedDate");
        if (processedTimestamp != null) {
            claim.setProcessedDate(processedTimestamp.toLocalDateTime());
        }
        
        return claim;
    }
}

