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

public class InsuranceClaim {
    private int claimID;
    private int patientID;
    private int treatmentID;
    private Double claimAmount;
    private Double approvedAmount;
    private String status;
    private String claimReason;
    private String rejectionReason;
    private Integer processedBy;
    private LocalDateTime submittedDate;
    private LocalDateTime processedDate;
    private Patient patient;
    private Treatment treatment;
    private Employee processedByEmployee;
    
    public InsuranceClaim() {
        this.status = "SUBMITTED";
        this.submittedDate = LocalDateTime.now();
    }
    
    public InsuranceClaim(int patientID, int treatmentID, Double claimAmount, String claimReason) {
        this.patientID = patientID;
        this.treatmentID = treatmentID;
        this.claimAmount = claimAmount;
        this.claimReason = claimReason;
        this.status = "SUBMITTED";
        this.submittedDate = LocalDateTime.now();
    }
    
    public int getClaimID() { return claimID; }
    public void setClaimID(int claimID) { this.claimID = claimID; }
    
    public int getPatientID() { return patientID; }
    public void setPatientID(int patientID) { this.patientID = patientID; }
    
    public int getTreatmentID() { return treatmentID; }
    public void setTreatmentID(int treatmentID) { this.treatmentID = treatmentID; }
    
    public Double getClaimAmount() { return claimAmount; }
    public void setClaimAmount(Double claimAmount) { this.claimAmount = claimAmount; }
    
    public Double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(Double approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public Integer getProcessedBy() { return processedBy; }
    public void setProcessedBy(Integer processedBy) { this.processedBy = processedBy; }
    
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }
    
    public LocalDateTime getProcessedDate() { return processedDate; }
    public void setProcessedDate(LocalDateTime processedDate) { this.processedDate = processedDate; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }
    
    public Employee getProcessedByEmployee() { return processedByEmployee; }
    public void setProcessedByEmployee(Employee processedByEmployee) { this.processedByEmployee = processedByEmployee; }
    
    @Override
    public String toString() {
        return "InsuranceClaim{claimID=" + claimID + ", claimAmount=" + claimAmount + ", status='" + status + "'}";
    }
}