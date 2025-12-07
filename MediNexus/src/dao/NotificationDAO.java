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
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public boolean createNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (UserID, Message, NotificationType, IsRead, " +
                     "RelatedEntityType, RelatedEntityID, CreatedDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, notification.getUserID());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getNotificationType());
            ps.setBoolean(4, notification.isRead());
            ps.setString(5, notification.getRelatedEntityType());
            
            if (notification.getRelatedEntityID() != null) {
                ps.setInt(6, notification.getRelatedEntityID());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    notification.setNotificationID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Notification getNotificationByID(int notificationID) {
        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractNotificationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Notification> getNotificationsByUserID(int userID) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? ORDER BY CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notifications;
    }
    
    public List<Notification> getUnreadNotificationsByUserID(int userID) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? AND IsRead = 0 ORDER BY CreatedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notifications;
    }
    
    public int getUnreadNotificationCount(int userID) {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE UserID = ? AND IsRead = 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    public boolean markAsRead(int notificationID) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean markAllAsReadForUser(int userID) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE UserID = ? AND IsRead = 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteNotification(int notificationID) {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteAllNotificationsForUser(int userID) {
        String sql = "DELETE FROM Notifications WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationID(rs.getInt("NotificationID"));
        notification.setUserID(rs.getInt("UserID"));
        notification.setMessage(rs.getString("Message"));
        notification.setNotificationType(rs.getString("NotificationType"));
        notification.setRead(rs.getBoolean("IsRead"));
        notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
        
        int relatedEntityID = rs.getInt("RelatedEntityID");
        if (!rs.wasNull()) {
            notification.setRelatedEntityID(relatedEntityID);
        }
        
        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            notification.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        return notification;
    }
}

