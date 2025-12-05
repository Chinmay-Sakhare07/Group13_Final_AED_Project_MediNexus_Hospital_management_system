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

public class Report {
    private int reportID;
    private int generatedBy;
    private String reportType;
    private String reportTitle;
    private String reportContent;
    private String filePath;
    private LocalDateTime generatedDate;
    private Employee generatedByEmployee;
    
    public Report() {
        this.generatedDate = LocalDateTime.now();
    }
    
    public Report(int generatedBy, String reportType, String reportTitle) {
        this.generatedBy = generatedBy;
        this.reportType = reportType;
        this.reportTitle = reportTitle;
        this.generatedDate = LocalDateTime.now();
    }
    
    public int getReportID() { return reportID; }
    public void setReportID(int reportID) { this.reportID = reportID; }
    
    public int getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(int generatedBy) { this.generatedBy = generatedBy; }
    
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    
    public String getReportTitle() { return reportTitle; }
    public void setReportTitle(String reportTitle) { this.reportTitle = reportTitle; }
    
    public String getReportContent() { return reportContent; }
    public void setReportContent(String reportContent) { this.reportContent = reportContent; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    
    public Employee getGeneratedByEmployee() { return generatedByEmployee; }
    public void setGeneratedByEmployee(Employee generatedByEmployee) { this.generatedByEmployee = generatedByEmployee; }
    
    @Override
    public String toString() {
        return "Report{reportID=" + reportID + ", type='" + reportType + "', title='" + reportTitle + "'}";
    }
}
