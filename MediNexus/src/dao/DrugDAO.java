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
import model.Drug;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugDAO {
    
    public boolean createDrug(Drug drug) {
        String sql = "INSERT INTO Drugs (DrugName, GenericName, Category, Manufacturer, DosageForm, " +
                     "Strength, UnitPrice, StockQuantity, Description, SideEffects) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, drug.getDrugName());
            ps.setString(2, drug.getGenericName());
            ps.setString(3, drug.getCategory());
            ps.setString(4, drug.getManufacturer());
            ps.setString(5, drug.getDosageForm());
            ps.setString(6, drug.getStrength());
            ps.setDouble(7, drug.getUnitPrice());
            ps.setInt(8, drug.getStockQuantity());
            ps.setString(9, drug.getDescription());
            ps.setString(10, drug.getSideEffects());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    drug.setDrugID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Drug getDrugByID(int drugID) {
        String sql = "SELECT * FROM Drugs WHERE DrugID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, drugID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractDrugFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Drug> searchDrugsByName(String drugName) {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM Drugs WHERE DrugName LIKE ? OR GenericName LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + drugName + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                drugs.add(extractDrugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return drugs;
    }
    
    public List<Drug> getDrugsByCategory(String category) {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM Drugs WHERE Category = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                drugs.add(extractDrugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return drugs;
    }
    
    public List<Drug> getAllDrugs() {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM Drugs ORDER BY DrugName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                drugs.add(extractDrugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return drugs;
    }
    
    public List<Drug> getLowStockDrugs(int threshold) {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM Drugs WHERE StockQuantity <= ? ORDER BY StockQuantity";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                drugs.add(extractDrugFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return drugs;
    }
    
    public boolean updateDrug(Drug drug) {
        String sql = "UPDATE Drugs SET DrugName = ?, GenericName = ?, Category = ?, Manufacturer = ?, " +
                     "DosageForm = ?, Strength = ?, UnitPrice = ?, StockQuantity = ?, Description = ?, " +
                     "SideEffects = ? WHERE DrugID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, drug.getDrugName());
            ps.setString(2, drug.getGenericName());
            ps.setString(3, drug.getCategory());
            ps.setString(4, drug.getManufacturer());
            ps.setString(5, drug.getDosageForm());
            ps.setString(6, drug.getStrength());
            ps.setDouble(7, drug.getUnitPrice());
            ps.setInt(8, drug.getStockQuantity());
            ps.setString(9, drug.getDescription());
            ps.setString(10, drug.getSideEffects());
            ps.setInt(11, drug.getDrugID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateStock(int drugID, int quantity) {
        String sql = "UPDATE Drugs SET StockQuantity = StockQuantity + ? WHERE DrugID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, drugID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteDrug(int drugID) {
        String sql = "DELETE FROM Drugs WHERE DrugID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, drugID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Drug extractDrugFromResultSet(ResultSet rs) throws SQLException {
        Drug drug = new Drug();
        drug.setDrugID(rs.getInt("DrugID"));
        drug.setDrugName(rs.getString("DrugName"));
        drug.setGenericName(rs.getString("GenericName"));
        drug.setCategory(rs.getString("Category"));
        drug.setManufacturer(rs.getString("Manufacturer"));
        drug.setDosageForm(rs.getString("DosageForm"));
        drug.setStrength(rs.getString("Strength"));
        drug.setUnitPrice(rs.getDouble("UnitPrice"));
        drug.setStockQuantity(rs.getInt("StockQuantity"));
        drug.setDescription(rs.getString("Description"));
        drug.setSideEffects(rs.getString("SideEffects"));
        
        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            drug.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        return drug;
    }
}
