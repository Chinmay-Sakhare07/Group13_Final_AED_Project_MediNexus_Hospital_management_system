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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.VitalSigns;

public class VitalSignsDAO {
    
    public boolean createVitalSigns(VitalSigns vitalSigns) {
        String sql = "INSERT INTO VitalSigns (ComplaintID, PatientID, BloodPressureSystolic, BloodPressureDiastolic, " +
                     "HeartRate, Temperature, RespiratoryRate, OxygenSaturation, Weight, RecordedBy, RecordedDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, vitalSigns.getComplaintID());
            ps.setInt(2, vitalSigns.getPatientID());
            
            if (vitalSigns.getBloodPressureSystolic() != null) {
                ps.setInt(3, vitalSigns.getBloodPressureSystolic());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            if (vitalSigns.getBloodPressureDiastolic() != null) {
                ps.setInt(4, vitalSigns.getBloodPressureDiastolic());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            if (vitalSigns.getHeartRate() != null) {
                ps.setInt(5, vitalSigns.getHeartRate());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            
            if (vitalSigns.getTemperature() != null) {
                ps.setDouble(6, vitalSigns.getTemperature());
            } else {
                ps.setNull(6, Types.DOUBLE);
            }
            
            if (vitalSigns.getRespiratoryRate() != null) {
                ps.setInt(7, vitalSigns.getRespiratoryRate());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            
            if (vitalSigns.getOxygenSaturation() != null) {
                ps.setInt(8, vitalSigns.getOxygenSaturation());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            
            if (vitalSigns.getWeight() != null) {
                ps.setDouble(9, vitalSigns.getWeight());
            } else {
                ps.setNull(9, Types.DOUBLE);
            }
            
            if (vitalSigns.getRecordedBy() != null) {
                ps.setInt(10, vitalSigns.getRecordedBy());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    vitalSigns.setVitalSignID(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public VitalSigns getVitalSignsByID(int vitalSignID) {
        String sql = "SELECT * FROM VitalSigns WHERE VitalSignID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vitalSignID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractVitalSignsFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<VitalSigns> getVitalSignsByComplaintID(int complaintID) {
        List<VitalSigns> vitalSignsList = new ArrayList<>();
        String sql = "SELECT * FROM VitalSigns WHERE ComplaintID = ? ORDER BY RecordedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, complaintID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                vitalSignsList.add(extractVitalSignsFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vitalSignsList;
    }
    
    public List<VitalSigns> getVitalSignsByPatientID(int patientID) {
        List<VitalSigns> vitalSignsList = new ArrayList<>();
        String sql = "SELECT * FROM VitalSigns WHERE PatientID = ? ORDER BY RecordedDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                vitalSignsList.add(extractVitalSignsFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vitalSignsList;
    }
    
    public boolean updateVitalSigns(VitalSigns vitalSigns) {
        String sql = "UPDATE VitalSigns SET BloodPressureSystolic = ?, BloodPressureDiastolic = ?, " +
                     "HeartRate = ?, Temperature = ?, RespiratoryRate = ?, OxygenSaturation = ?, Weight = ? " +
                     "WHERE VitalSignID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (vitalSigns.getBloodPressureSystolic() != null) {
                ps.setInt(1, vitalSigns.getBloodPressureSystolic());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            
            if (vitalSigns.getBloodPressureDiastolic() != null) {
                ps.setInt(2, vitalSigns.getBloodPressureDiastolic());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            if (vitalSigns.getHeartRate() != null) {
                ps.setInt(3, vitalSigns.getHeartRate());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            if (vitalSigns.getTemperature() != null) {
                ps.setDouble(4, vitalSigns.getTemperature());
            } else {
                ps.setNull(4, Types.DOUBLE);
            }
            
            if (vitalSigns.getRespiratoryRate() != null) {
                ps.setInt(5, vitalSigns.getRespiratoryRate());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            
            if (vitalSigns.getOxygenSaturation() != null) {
                ps.setInt(6, vitalSigns.getOxygenSaturation());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            if (vitalSigns.getWeight() != null) {
                ps.setDouble(7, vitalSigns.getWeight());
            } else {
                ps.setNull(7, Types.DOUBLE);
            }
            
            ps.setInt(8, vitalSigns.getVitalSignID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteVitalSigns(int vitalSignID) {
        String sql = "DELETE FROM VitalSigns WHERE VitalSignID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vitalSignID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private VitalSigns extractVitalSignsFromResultSet(ResultSet rs) throws SQLException {
        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setVitalSignID(rs.getInt("VitalSignID"));
        vitalSigns.setComplaintID(rs.getInt("ComplaintID"));
        vitalSigns.setPatientID(rs.getInt("PatientID"));
        
        int bpSystolic = rs.getInt("BloodPressureSystolic");
        if (!rs.wasNull()) vitalSigns.setBloodPressureSystolic(bpSystolic);
        
        int bpDiastolic = rs.getInt("BloodPressureDiastolic");
        if (!rs.wasNull()) vitalSigns.setBloodPressureDiastolic(bpDiastolic);
        
        int heartRate = rs.getInt("HeartRate");
        if (!rs.wasNull()) vitalSigns.setHeartRate(heartRate);
        
        double temperature = rs.getDouble("Temperature");
        if (!rs.wasNull()) vitalSigns.setTemperature(temperature);
        
        int respRate = rs.getInt("RespiratoryRate");
        if (!rs.wasNull()) vitalSigns.setRespiratoryRate(respRate);
        
        int o2Sat = rs.getInt("OxygenSaturation");
        if (!rs.wasNull()) vitalSigns.setOxygenSaturation(o2Sat);
        
        double weight = rs.getDouble("Weight");
        if (!rs.wasNull()) vitalSigns.setWeight(weight);
        
        int recordedBy = rs.getInt("RecordedBy");
        if (!rs.wasNull()) vitalSigns.setRecordedBy(recordedBy);
        
        Timestamp recordedTimestamp = rs.getTimestamp("RecordedDate");
        if (recordedTimestamp != null) {
            vitalSigns.setRecordedDate(recordedTimestamp.toLocalDateTime());
        }
        
        return vitalSigns;
    }
}