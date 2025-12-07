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
import model.SpecialistReferral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialistReferralDAO {
    
    public boolean createReferral(SpecialistReferral referral) {
        String sql = "INSERT INTO SpecialistReferrals (DiagnosisID, ReferredBy, ReferredTo, ReferralReason, " +
                     "Status, ReferralDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, referral.getDiagnosisID());
            ps.setInt(2, referral.getReferredBy());
            
            if (referral.getReferredTo() != null) {
                ps.setInt(3, referral.getReferredTo());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setString(4, referral.getReferralReason());
            ps.setString(5, referral.getStatus());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    referral.setReferralID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public SpecialistReferral getReferralByID(int referralID) {
        String sql = "SELECT * FROM SpecialistReferrals WHERE ReferralID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, referralID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractReferralFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public SpecialistReferral getReferralByDiagnosisID(int diagnosisID) {
        String sql = "SELECT * FROM SpecialistReferrals WHERE DiagnosisID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, diagnosisID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractReferralFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<SpecialistReferral> getReferralsBySpecialistID(int specialistID) {
        List<SpecialistReferral> referrals = new ArrayList<>();
        String sql = "SELECT * FROM SpecialistReferrals WHERE ReferredTo = ? ORDER BY ReferralDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, specialistID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                referrals.add(extractReferralFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return referrals;
    }
    
    public List<SpecialistReferral> getReferralsByDoctorID(int doctorID) {
        List<SpecialistReferral> referrals = new ArrayList<>();
        String sql = "SELECT * FROM SpecialistReferrals WHERE ReferredBy = ? ORDER BY ReferralDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                referrals.add(extractReferralFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return referrals;
    }
    
    public List<SpecialistReferral> getReferralsByStatus(String status) {
        List<SpecialistReferral> referrals = new ArrayList<>();
        String sql = "SELECT * FROM SpecialistReferrals WHERE Status = ? ORDER BY ReferralDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                referrals.add(extractReferralFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return referrals;
    }
    
    public boolean assignReferralToSpecialist(int referralID, int specialistID) {
        String sql = "UPDATE SpecialistReferrals SET ReferredTo = ?, Status = 'ACCEPTED' WHERE ReferralID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, specialistID);
            ps.setInt(2, referralID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateReferralStatus(int referralID, String status, String specialistNotes) {
        String sql = "UPDATE SpecialistReferrals SET Status = ?, SpecialistNotes = ?, CompletedDate = GETDATE() " +
                     "WHERE ReferralID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setString(2, specialistNotes);
            ps.setInt(3, referralID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateReferral(SpecialistReferral referral) {
        String sql = "UPDATE SpecialistReferrals SET ReferredTo = ?, ReferralReason = ?, Status = ?, " +
                     "SpecialistNotes = ? WHERE ReferralID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (referral.getReferredTo() != null) {
                ps.setInt(1, referral.getReferredTo());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            
            ps.setString(2, referral.getReferralReason());
            ps.setString(3, referral.getStatus());
            ps.setString(4, referral.getSpecialistNotes());
            ps.setInt(5, referral.getReferralID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteReferral(int referralID) {
        String sql = "DELETE FROM SpecialistReferrals WHERE ReferralID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, referralID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private SpecialistReferral extractReferralFromResultSet(ResultSet rs) throws SQLException {
        SpecialistReferral referral = new SpecialistReferral();
        referral.setReferralID(rs.getInt("ReferralID"));
        referral.setDiagnosisID(rs.getInt("DiagnosisID"));
        referral.setReferredBy(rs.getInt("ReferredBy"));
        
        int referredTo = rs.getInt("ReferredTo");
        if (!rs.wasNull()) {
            referral.setReferredTo(referredTo);
        }
        
        referral.setReferralReason(rs.getString("ReferralReason"));
        referral.setStatus(rs.getString("Status"));
        referral.setSpecialistNotes(rs.getString("SpecialistNotes"));
        
        Timestamp referralTimestamp = rs.getTimestamp("ReferralDate");
        if (referralTimestamp != null) {
            referral.setReferralDate(referralTimestamp.toLocalDateTime());
        }
        
        Timestamp completedTimestamp = rs.getTimestamp("CompletedDate");
        if (completedTimestamp != null) {
            referral.setCompletedDate(completedTimestamp.toLocalDateTime());
        }
        
        return referral;
    }
}
