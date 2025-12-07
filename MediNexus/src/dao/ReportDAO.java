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
import model.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    
    public boolean createReport(Report report) {
        String sql = "INSERT INTO Reports (GeneratedBy, ReportType, ReportTitle, ReportContent, " +
                     "FilePath, GeneratedDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, report.getGeneratedBy());
            ps.setString(2, report.getReportType());
            ps.setString(3, report.getReportTitle());
            ps.setString(4, report.getReportContent());
            ps.setString(5, report.getFilePath());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    report.setReportID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Report getReportByID(int reportID) {
        String sql = "SELECT * FROM Reports WHERE ReportID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reportID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractReportFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Report> getReportsByGeneratorID(int generatorID) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Reports WHERE GeneratedBy = ? ORDER BY GeneratedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, generatorID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    public List<Report> getReportsByType(String reportType) {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Reports WHERE ReportType = ? ORDER BY GeneratedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, reportType);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Reports ORDER BY GeneratedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    public boolean updateReport(Report report) {
        String sql = "UPDATE Reports SET ReportType = ?, ReportTitle = ?, ReportContent = ?, " +
                     "FilePath = ? WHERE ReportID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, report.getReportType());
            ps.setString(2, report.getReportTitle());
            ps.setString(3, report.getReportContent());
            ps.setString(4, report.getFilePath());
            ps.setInt(5, report.getReportID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteReport(int reportID) {
        String sql = "DELETE FROM Reports WHERE ReportID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reportID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Report extractReportFromResultSet(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setReportID(rs.getInt("ReportID"));
        report.setGeneratedBy(rs.getInt("GeneratedBy"));
        report.setReportType(rs.getString("ReportType"));
        report.setReportTitle(rs.getString("ReportTitle"));
        report.setReportContent(rs.getString("ReportContent"));
        report.setFilePath(rs.getString("FilePath"));
        
        Timestamp generatedTimestamp = rs.getTimestamp("GeneratedDate");
        if (generatedTimestamp != null) {
            report.setGeneratedDate(generatedTimestamp.toLocalDateTime());
        }
        
        return report;
    }
}

