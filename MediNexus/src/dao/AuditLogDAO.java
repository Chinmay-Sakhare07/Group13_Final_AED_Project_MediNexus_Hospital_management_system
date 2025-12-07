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
import model.AuditLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {
    
    public boolean createAuditLog(AuditLog auditLog) {
        String sql = "INSERT INTO AuditLogs (UserID, Action, EntityType, EntityID, Details, IPAddress, Timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, auditLog.getUserID());
            ps.setString(2, auditLog.getAction());
            ps.setString(3, auditLog.getEntityType());
            
            if (auditLog.getEntityID() != null) {
                ps.setInt(4, auditLog.getEntityID());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            ps.setString(5, auditLog.getDetails());
            ps.setString(6, auditLog.getIpAddress());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    auditLog.setLogID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public AuditLog getAuditLogByID(int logID) {
        String sql = "SELECT * FROM AuditLogs WHERE LogID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, logID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractAuditLogFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<AuditLog> getAuditLogsByUserID(int userID) {
        List<AuditLog> auditLogs = new ArrayList<>();
        String sql = "SELECT * FROM AuditLogs WHERE UserID = ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                auditLogs.add(extractAuditLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    public List<AuditLog> getAuditLogsByAction(String action) {
        List<AuditLog> auditLogs = new ArrayList<>();
        String sql = "SELECT * FROM AuditLogs WHERE Action = ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, action);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                auditLogs.add(extractAuditLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        List<AuditLog> auditLogs = new ArrayList<>();
        String sql = "SELECT * FROM AuditLogs WHERE EntityType = ? ORDER BY Timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                auditLogs.add(extractAuditLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    public List<AuditLog> getAllAuditLogs() {
        List<AuditLog> auditLogs = new ArrayList<>();
        String sql = "SELECT TOP 1000 * FROM AuditLogs ORDER BY Timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                auditLogs.add(extractAuditLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    public List<AuditLog> getRecentAuditLogs(int limit) {
        List<AuditLog> auditLogs = new ArrayList<>();
        String sql = "SELECT TOP ? * FROM AuditLogs ORDER BY Timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                auditLogs.add(extractAuditLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    public boolean deleteAuditLog(int logID) {
        String sql = "DELETE FROM AuditLogs WHERE LogID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, logID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteOldAuditLogs(int daysOld) {
        String sql = "DELETE FROM AuditLogs WHERE Timestamp < DATEADD(DAY, -?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, daysOld);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private AuditLog extractAuditLogFromResultSet(ResultSet rs) throws SQLException {
        AuditLog auditLog = new AuditLog();
        auditLog.setLogID(rs.getInt("LogID"));
        auditLog.setUserID(rs.getInt("UserID"));
        auditLog.setAction(rs.getString("Action"));
        auditLog.setEntityType(rs.getString("EntityType"));
        
        int entityID = rs.getInt("EntityID");
        if (!rs.wasNull()) {
            auditLog.setEntityID(entityID);
        }
        
        auditLog.setDetails(rs.getString("Details"));
        auditLog.setIpAddress(rs.getString("IPAddress"));
        
        Timestamp timestamp = rs.getTimestamp("Timestamp");
        if (timestamp != null) {
            auditLog.setTimestamp(timestamp.toLocalDateTime());
        }
        
        return auditLog;
    }
}

