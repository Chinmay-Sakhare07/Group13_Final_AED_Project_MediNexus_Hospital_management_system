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
import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    // CREATE - Add new employee
    public boolean createEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (UserID, FirstName, LastName, Specialization, LicenseNumber, OrganizationID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, employee.getUserID());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getSpecialization());
            ps.setString(5, employee.getLicenseNumber());
            ps.setInt(6, employee.getOrganizationID());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    employee.setEmployeeID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // READ - Get employee by ID
    public Employee getEmployeeByID(int employeeID) {
        String sql = "SELECT * FROM Employees WHERE EmployeeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get employee by UserID
    public Employee getEmployeeByUserID(int userID) {
        String sql = "SELECT * FROM Employees WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // READ - Get all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees ORDER BY LastName, FirstName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    // READ - Get employees by organization
    public List<Employee> getEmployeesByOrganization(int organizationID) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE OrganizationID = ? ORDER BY LastName, FirstName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, organizationID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    // READ - Get employees by specialization
    public List<Employee> getEmployeesBySpecialization(String specialization) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE Specialization = ? ORDER BY LastName, FirstName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, specialization);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    // UPDATE - Update employee information
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET FirstName = ?, LastName = ?, Specialization = ?, " +
                     "LicenseNumber = ?, OrganizationID = ? WHERE EmployeeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getSpecialization());
            ps.setString(4, employee.getLicenseNumber());
            ps.setInt(5, employee.getOrganizationID());
            ps.setInt(6, employee.getEmployeeID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // DELETE - Delete employee
    public boolean deleteEmployee(int employeeID) {
        String sql = "DELETE FROM Employees WHERE EmployeeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper method to extract Employee from ResultSet
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeID(rs.getInt("EmployeeID"));
        employee.setUserID(rs.getInt("UserID"));
        employee.setFirstName(rs.getString("FirstName"));
        employee.setLastName(rs.getString("LastName"));
        employee.setSpecialization(rs.getString("Specialization"));
        employee.setLicenseNumber(rs.getString("LicenseNumber"));
        employee.setOrganizationID(rs.getInt("OrganizationID"));
        
        return employee;
    }
}
