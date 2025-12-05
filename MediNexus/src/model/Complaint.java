/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Shreya
 */
import java.time.LocalDateTime;

public class Complaint {
    private int complaintID;
    private int patientID;
    private String category;
    private String description;
    private String status;
    private String priority;
    private Integer assignedDoctorID;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private Patient patient;
    private Employee assignedDoctor;
    
    public Complaint() {
        this.status = "SUBMITTED";
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public Complaint(int patientID, String category, String description, String priority) {
        this.patientID = patientID;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = "SUBMITTED";
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public int getComplaintID() { return complaintID; }
    public void setComplaintID(int complaintID) { this.complaintID = complaintID; }
    
    public int getPatientID() { return patientID; }
    public void setPatientID(int patientID) { this.patientID = patientID; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public Integer getAssignedDoctorID() { return assignedDoctorID; }
    public void setAssignedDoctorID(Integer assignedDoctorID) { this.assignedDoctorID = assignedDoctorID; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Employee getAssignedDoctor() { return assignedDoctor; }
    public void setAssignedDoctor(Employee assignedDoctor) { this.assignedDoctor = assignedDoctor; }
    
    @Override
    public String toString() {
        return "Complaint{complaintID=" + complaintID + ", category='" + category + "', status='" + status + "', priority='" + priority + "'}";
    }
}



