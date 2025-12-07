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
import model.Enterprise;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnterpriseDAO {

    public boolean createEnterprise(Enterprise enterprise) {
        String sql = "INSERT INTO Enterprises (EnterpriseName, EnterpriseType) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, enterprise.getEnterpriseName());
            ps.setString(2, enterprise.getEnterpriseType());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    enterprise.setEnterpriseID(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Enterprise getEnterpriseByID(int enterpriseID) {
        String sql = "SELECT * FROM Enterprises WHERE EnterpriseID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enterpriseID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractEnterpriseFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Enterprise> getAllEnterprises() {
        List<Enterprise> enterprises = new ArrayList<>();
        String sql = "SELECT * FROM Enterprises ORDER BY EnterpriseName";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                enterprises.add(extractEnterpriseFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enterprises;
    }

    public List<Enterprise> getEnterprisesByType(String type) {
        List<Enterprise> enterprises = new ArrayList<>();
        String sql = "SELECT * FROM Enterprises WHERE EnterpriseType = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                enterprises.add(extractEnterpriseFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enterprises;
    }

    public boolean updateEnterprise(Enterprise enterprise) {
        String sql = "UPDATE Enterprises SET EnterpriseName = ?, EnterpriseType = ? WHERE EnterpriseID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, enterprise.getEnterpriseName());
            ps.setString(2, enterprise.getEnterpriseType());
            ps.setInt(3, enterprise.getEnterpriseID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteEnterprise(int enterpriseID) {
        String sql = "DELETE FROM Enterprises WHERE EnterpriseID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enterpriseID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Enterprise extractEnterpriseFromResultSet(ResultSet rs) throws SQLException {
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseID(rs.getInt("EnterpriseID"));
        enterprise.setEnterpriseName(rs.getString("EnterpriseName"));
        enterprise.setEnterpriseType(rs.getString("EnterpriseType"));

        Timestamp createdTimestamp = rs.getTimestamp("CreatedDate");
        if (createdTimestamp != null) {
            enterprise.setCreatedDate(createdTimestamp.toLocalDateTime());
        }

        return enterprise;
    }
}
