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

public class SpecialistReferral {
    private int referralID;
    private int diagnosisID;
    private int referredBy;
    private Integer referredTo;
    private String referralReason;
    private String status;
    private String specialistNotes;
    private LocalDateTime referralDate;
    private LocalDateTime completedDate;
    private Diagnosis diagnosis;
    private Employee referredByDoctor;
    private Employee referredToSpecialist;
    
    public SpecialistReferral() {
        this.status = "PENDING";
        this.referralDate = LocalDateTime.now();
    }
    
    public SpecialistReferral(int diagnosisID, int referredBy, String referralReason) {
        this.diagnosisID = diagnosisID;
        this.referredBy = referredBy;
        this.referralReason = referralReason;
        this.status = "PENDING";
        this.referralDate = LocalDateTime.now();
    }
    
    public int getReferralID() { return referralID; }
    public void setReferralID(int referralID) { this.referralID = referralID; }
    
    public int getDiagnosisID() { return diagnosisID; }
    public void setDiagnosisID(int diagnosisID) { this.diagnosisID = diagnosisID; }
    
    public int getReferredBy() { return referredBy; }
    public void setReferredBy(int referredBy) { this.referredBy = referredBy; }
    
    public Integer getReferredTo() { return referredTo; }
    public void setReferredTo(Integer referredTo) { this.referredTo = referredTo; }
    
    public String getReferralReason() { return referralReason; }
    public void setReferralReason(String referralReason) { this.referralReason = referralReason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSpecialistNotes() { return specialistNotes; }
    public void setSpecialistNotes(String specialistNotes) { this.specialistNotes = specialistNotes; }
    
    public LocalDateTime getReferralDate() { return referralDate; }
    public void setReferralDate(LocalDateTime referralDate) { this.referralDate = referralDate; }
    
    public LocalDateTime getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDateTime completedDate) { this.completedDate = completedDate; }
    
    public Diagnosis getDiagnosis() { return diagnosis; }
    public void setDiagnosis(Diagnosis diagnosis) { this.diagnosis = diagnosis; }
    
    public Employee getReferredByDoctor() { return referredByDoctor; }
    public void setReferredByDoctor(Employee referredByDoctor) { this.referredByDoctor = referredByDoctor; }
    
    public Employee getReferredToSpecialist() { return referredToSpecialist; }
    public void setReferredToSpecialist(Employee referredToSpecialist) { this.referredToSpecialist = referredToSpecialist; }
    
    @Override
    public String toString() {
        return "SpecialistReferral{referralID=" + referralID + ", status='" + status + "', referralDate=" + referralDate + "}";
    }
}

