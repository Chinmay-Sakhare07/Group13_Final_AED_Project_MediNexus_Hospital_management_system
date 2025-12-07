/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Chinmay
 */
import config.DatabaseConnection;
import model.Organization;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {
    
    public boolean createOrganization(Organization organization) {
        String sql = "INSERT INTO Organizations (OrganizationName, OrganizationType, EnterpriseID) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, organization.getOrganizationName());
            ps.setString(2, organization.getOrganizationType());
            ps.setInt(3, organization.getEnterpriseID());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    organization.setOrganizationID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Organization getOrganizationByID(int organizationID) {
        String sql = "SELECT * FROM Organizations WHERE OrganizationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, organizationID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractOrganizationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT * FROM Organizations ORDER BY OrganizationName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                organizations.add(extractOrganizationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return organizations;
    }
    
    public List<Organization> getOrganizationsByEnterprise(int enterpriseID) {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT * FROM Organizations WHERE EnterpriseID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, enterpriseID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                organizations.add(extractOrganizationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return organizations;
    }
    
    public List<Organization> getOrganizationsByType(String type) {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT * FROM Organizations WHERE OrganizationType = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                organizations.add(extractOrganizationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return organizations;
    }
    
    public boolean updateOrganization(Organization organization) {
        String sql = "UPDATE Organizations SET OrganizationName = ?, OrganizationType = ?, EnterpriseID = ? WHERE OrganizationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, organization.getOrganizationName());
            ps.setString(2, organization.getOrganizationType());
            ps.setInt(3, organization.getEnterpriseID());
            ps.setInt(4, organization.getOrganizationID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteOrganization(int organizationID) {
        String sql = "DELETE FROM Organizations WHERE OrganizationID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, organizationID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Organization extractOrganizationFromResultSet(ResultSet rs) throws SQLException {
        Organization organization = new Organization();
        organization.setOrganizationID(rs.getInt("OrganizationID"));
        organization.setOrganizationName(rs.getString("OrganizationName"));
        organization.setOrganizationType(rs.getString("OrganizationType"));
        organization.setEnterpriseID(rs.getInt("EnterpriseID"));
        
        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            organization.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        return organization;
    }
}