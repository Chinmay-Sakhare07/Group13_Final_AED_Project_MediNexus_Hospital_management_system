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
import model.MedicationOrder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationOrderDAO {
    
    public boolean createMedicationOrder(MedicationOrder order) {
        String sql = "INSERT INTO MedicationOrders (TreatmentID, DrugID, Quantity, Dosage, Frequency, " +
                     "Duration, Status, OrderDate) VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, order.getTreatmentID());
            ps.setInt(2, order.getDrugID());
            ps.setInt(3, order.getQuantity());
            ps.setString(4, order.getDosage());
            ps.setString(5, order.getFrequency());
            ps.setString(6, order.getDuration());
            ps.setString(7, order.getStatus());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    order.setOrderID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public MedicationOrder getMedicationOrderByID(int orderID) {
        String sql = "SELECT * FROM MedicationOrders WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractMedicationOrderFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<MedicationOrder> getMedicationOrdersByTreatmentID(int treatmentID) {
        List<MedicationOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM MedicationOrders WHERE TreatmentID = ? ORDER BY OrderDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, treatmentID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(extractMedicationOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }
    
    public List<MedicationOrder> getMedicationOrdersByStatus(String status) {
        List<MedicationOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM MedicationOrders WHERE Status = ? ORDER BY OrderDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(extractMedicationOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }
    
    public List<MedicationOrder> getMedicationOrdersByPharmacistID(int pharmacistID) {
        List<MedicationOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM MedicationOrders WHERE PharmacistID = ? ORDER BY FulfilledDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, pharmacistID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(extractMedicationOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }
    
    public List<MedicationOrder> getAllMedicationOrders() {
        List<MedicationOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM MedicationOrders ORDER BY OrderDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(extractMedicationOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }
    
    public boolean updateMedicationOrderStatus(int orderID, String status, Integer pharmacistID) {
        String sql = "UPDATE MedicationOrders SET Status = ?, PharmacistID = ?, FulfilledDate = GETDATE() " +
                     "WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            
            if (pharmacistID != null) {
                ps.setInt(2, pharmacistID);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setInt(3, orderID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateMedicationOrder(MedicationOrder order) {
        String sql = "UPDATE MedicationOrders SET Quantity = ?, Dosage = ?, Frequency = ?, " +
                     "Duration = ?, Status = ? WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, order.getQuantity());
            ps.setString(2, order.getDosage());
            ps.setString(3, order.getFrequency());
            ps.setString(4, order.getDuration());
            ps.setString(5, order.getStatus());
            ps.setInt(6, order.getOrderID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteMedicationOrder(int orderID) {
        String sql = "DELETE FROM MedicationOrders WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private MedicationOrder extractMedicationOrderFromResultSet(ResultSet rs) throws SQLException {
        MedicationOrder order = new MedicationOrder();
        order.setOrderID(rs.getInt("OrderID"));
        order.setTreatmentID(rs.getInt("TreatmentID"));
        order.setDrugID(rs.getInt("DrugID"));
        order.setQuantity(rs.getInt("Quantity"));
        order.setDosage(rs.getString("Dosage"));
        order.setFrequency(rs.getString("Frequency"));
        order.setDuration(rs.getString("Duration"));
        order.setStatus(rs.getString("Status"));
        
        int pharmacistID = rs.getInt("PharmacistID");
        if (!rs.wasNull()) {
            order.setPharmacistID(pharmacistID);
        }
        
        Timestamp orderTimestamp = rs.getTimestamp("OrderDate");
        if (orderTimestamp != null) {
            order.setOrderDate(orderTimestamp.toLocalDateTime());
        }
        
        Timestamp fulfilledTimestamp = rs.getTimestamp("FulfilledDate");
        if (fulfilledTimestamp != null) {
            order.setFulfilledDate(fulfilledTimestamp.toLocalDateTime());
        }
        
        return order;
    }
}

