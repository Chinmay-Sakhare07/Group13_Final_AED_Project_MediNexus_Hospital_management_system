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
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // CREATE - Add new user
    public boolean createUser(User user) {
        String sql = "INSERT INTO Users (Username, PasswordHash, Role, Email, Phone, IsActive) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setBoolean(6, user.isActive());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // READ - Get user by username (for login)
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get user by ID
    public User getUserByID(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    // READ - Get users by role
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Role = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    // UPDATE - Update user information
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET Email = ?, Phone = ?, IsActive = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPhone());
            ps.setBoolean(3, user.isActive());
            ps.setInt(4, user.getUserID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // UPDATE - Update password
    public boolean updatePassword(int userID, String newPasswordHash) {
        String sql = "UPDATE Users SET PasswordHash = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // UPDATE - Update last login time
    public boolean updateLastLogin(int userID) {
        String sql = "UPDATE Users SET LastLogin = GETDATE() WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // DELETE - Soft delete user (set IsActive = 0)
    public boolean deleteUser(int userID) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper method to extract User from ResultSet
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setRole(rs.getString("Role"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setActive(rs.getBoolean("IsActive"));
        
        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            user.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        Timestamp lastLoginTimestamp = rs.getTimestamp("LastLogin");
        if (lastLoginTimestamp != null) {
            user.setLastLogin(lastLoginTimestamp.toLocalDateTime());
        }
        
        return user;
    }
}
