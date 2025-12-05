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

public class Assessment {
    private int assessmentID;
    private int complaintID;
    private int doctorID;
    private String symptoms;
    private String physicalExamination;
    private String preliminaryNotes;
    private LocalDateTime assessmentDate;
    private Complaint complaint;
    private Employee doctor;
    
    public Assessment() {
        this.assessmentDate = LocalDateTime.now();
    }
    
    public Assessment(int complaintID, int doctorID, String symptoms) {
        this.complaintID = complaintID;
        this.doctorID = doctorID;
        this.symptoms = symptoms;
        this.assessmentDate = LocalDateTime.now();
    }
    
    public int getAssessmentID() { return assessmentID; }
    public void setAssessmentID(int assessmentID) { this.assessmentID = assessmentID; }
    
    public int getComplaintID() { return complaintID; }
    public void setComplaintID(int complaintID) { this.complaintID = complaintID; }
    
    public int getDoctorID() { return doctorID; }
    public void setDoctorID(int doctorID) { this.doctorID = doctorID; }
    
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getPhysicalExamination() { return physicalExamination; }
    public void setPhysicalExamination(String physicalExamination) { this.physicalExamination = physicalExamination; }
    
    public String getPreliminaryNotes() { return preliminaryNotes; }
    public void setPreliminaryNotes(String preliminaryNotes) { this.preliminaryNotes = preliminaryNotes; }
    
    public LocalDateTime getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDateTime assessmentDate) { this.assessmentDate = assessmentDate; }
    
    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
    
    public Employee getDoctor() { return doctor; }
    public void setDoctor(Employee doctor) { this.doctor = doctor; }
    
    @Override
    public String toString() {
        return "Assessment{assessmentID=" + assessmentID + ", complaintID=" + complaintID + ", assessmentDate=" + assessmentDate + "}";
    }
}