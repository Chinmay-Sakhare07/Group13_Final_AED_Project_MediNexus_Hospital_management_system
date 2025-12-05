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

public class Diagnosis {
    private int diagnosisID;
    private int assessmentID;
    private String diagnosisCode;
    private String diagnosisName;
    private String description;
    private String severity;
    private int diagnosedBy;
    private LocalDateTime diagnosisDate;
    private Assessment assessment;
    private Employee diagnosedByEmployee;
    
    public Diagnosis() {
        this.diagnosisDate = LocalDateTime.now();
    }
    
    public Diagnosis(int assessmentID, String diagnosisName, String severity, int diagnosedBy) {
        this.assessmentID = assessmentID;
        this.diagnosisName = diagnosisName;
        this.severity = severity;
        this.diagnosedBy = diagnosedBy;
        this.diagnosisDate = LocalDateTime.now();
    }
    
    public int getDiagnosisID() { return diagnosisID; }
    public void setDiagnosisID(int diagnosisID) { this.diagnosisID = diagnosisID; }
    
    public int getAssessmentID() { return assessmentID; }
    public void setAssessmentID(int assessmentID) { this.assessmentID = assessmentID; }
    
    public String getDiagnosisCode() { return diagnosisCode; }
    public void setDiagnosisCode(String diagnosisCode) { this.diagnosisCode = diagnosisCode; }
    
    public String getDiagnosisName() { return diagnosisName; }
    public void setDiagnosisName(String diagnosisName) { this.diagnosisName = diagnosisName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public int getDiagnosedBy() { return diagnosedBy; }
    public void setDiagnosedBy(int diagnosedBy) { this.diagnosedBy = diagnosedBy; }
    
    public LocalDateTime getDiagnosisDate() { return diagnosisDate; }
    public void setDiagnosisDate(LocalDateTime diagnosisDate) { this.diagnosisDate = diagnosisDate; }
    
    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }
    
    public Employee getDiagnosedByEmployee() { return diagnosedByEmployee; }
    public void setDiagnosedByEmployee(Employee diagnosedByEmployee) { this.diagnosedByEmployee = diagnosedByEmployee; }
    
    @Override
    public String toString() {
        return "Diagnosis{diagnosisID=" + diagnosisID + ", name='" + diagnosisName + "', severity='" + severity + "'}";
    }
}
