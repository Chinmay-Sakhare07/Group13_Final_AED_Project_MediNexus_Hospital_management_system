/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Shreya
 * 
 */
import java.time.LocalDateTime;

public class VitalSigns {
    private int vitalSignID;
    private int complaintID;
    private int patientID;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Double temperature;
    private Integer respiratoryRate;
    private Integer oxygenSaturation;
    private Double weight;
    private Integer recordedBy;
    private LocalDateTime recordedDate;
    private Complaint complaint;
    private Patient patient;
    private Employee recordedByEmployee;
    
    public VitalSigns() {
        this.recordedDate = LocalDateTime.now();
    }
    
    public VitalSigns(int complaintID, int patientID) {
        this.complaintID = complaintID;
        this.patientID = patientID;
        this.recordedDate = LocalDateTime.now();
    }
    
    public int getVitalSignID() { return vitalSignID; }
    public void setVitalSignID(int vitalSignID) { this.vitalSignID = vitalSignID; }
    
    public int getComplaintID() { return complaintID; }
    public void setComplaintID(int complaintID) { this.complaintID = complaintID; }
    
    public int getPatientID() { return patientID; }
    public void setPatientID(int patientID) { this.patientID = patientID; }
    
    public Integer getBloodPressureSystolic() { return bloodPressureSystolic; }
    public void setBloodPressureSystolic(Integer bloodPressureSystolic) { this.bloodPressureSystolic = bloodPressureSystolic; }
    
    public Integer getBloodPressureDiastolic() { return bloodPressureDiastolic; }
    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) { this.bloodPressureDiastolic = bloodPressureDiastolic; }
    
    public String getBloodPressure() {
        if (bloodPressureSystolic != null && bloodPressureDiastolic != null) {
            return bloodPressureSystolic + "/" + bloodPressureDiastolic;
        }
        return "N/A";
    }
    
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    
    public Integer getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(Integer respiratoryRate) { this.respiratoryRate = respiratoryRate; }
    
    public Integer getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Integer oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public Integer getRecordedBy() { return recordedBy; }
    public void setRecordedBy(Integer recordedBy) { this.recordedBy = recordedBy; }
    
    public LocalDateTime getRecordedDate() { return recordedDate; }
    public void setRecordedDate(LocalDateTime recordedDate) { this.recordedDate = recordedDate; }
    
    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Employee getRecordedByEmployee() { return recordedByEmployee; }
    public void setRecordedByEmployee(Employee recordedByEmployee) { this.recordedByEmployee = recordedByEmployee; }
    
    @Override
    public String toString() {
        return "VitalSigns{vitalSignID=" + vitalSignID + ", BP=" + getBloodPressure() + ", HR=" + heartRate + ", Temp=" + temperature + "}";
    }
}
