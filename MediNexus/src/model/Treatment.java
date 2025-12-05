/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Shreya
 */
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Treatment {
    private int treatmentID;
    private int diagnosisID;
    private String treatmentPlan;
    private String medicationsPrescribed;
    private String instructions;
    private LocalDate followUpDate;
    private int prescribedBy;
    private LocalDateTime prescribedDate;
    private Diagnosis diagnosis;
    private Employee prescribedByDoctor;
    
    public Treatment() {
        this.prescribedDate = LocalDateTime.now();
    }
    
    public Treatment(int diagnosisID, String treatmentPlan, int prescribedBy) {
        this.diagnosisID = diagnosisID;
        this.treatmentPlan = treatmentPlan;
        this.prescribedBy = prescribedBy;
        this.prescribedDate = LocalDateTime.now();
    }
    
    public int getTreatmentID() { return treatmentID; }
    public void setTreatmentID(int treatmentID) { this.treatmentID = treatmentID; }
    
    public int getDiagnosisID() { return diagnosisID; }
    public void setDiagnosisID(int diagnosisID) { this.diagnosisID = diagnosisID; }
    
    public String getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }
    
    public String getMedicationsPrescribed() { return medicationsPrescribed; }
    public void setMedicationsPrescribed(String medicationsPrescribed) { this.medicationsPrescribed = medicationsPrescribed; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    
    public int getPrescribedBy() { return prescribedBy; }
    public void setPrescribedBy(int prescribedBy) { this.prescribedBy = prescribedBy; }
    
    public LocalDateTime getPrescribedDate() { return prescribedDate; }
    public void setPrescribedDate(LocalDateTime prescribedDate) { this.prescribedDate = prescribedDate; }
    
    public Diagnosis getDiagnosis() { return diagnosis; }
    public void setDiagnosis(Diagnosis diagnosis) { this.diagnosis = diagnosis; }
    
    public Employee getPrescribedByDoctor() { return prescribedByDoctor; }
    public void setPrescribedByDoctor(Employee prescribedByDoctor) { this.prescribedByDoctor = prescribedByDoctor; }
    
    @Override
    public String toString() {
        return "Treatment{treatmentID=" + treatmentID + ", diagnosisID=" + diagnosisID + ", prescribedDate=" + prescribedDate + "}";
    }
}

