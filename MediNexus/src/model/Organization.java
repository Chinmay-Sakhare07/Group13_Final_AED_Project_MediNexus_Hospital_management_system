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

public class Organization {
    private int organizationID;
    private String organizationName;
    private String organizationType;
    private int enterpriseID;
    private LocalDateTime createdDate;
    private Enterprise enterprise;
    
    public Organization() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Organization(String organizationName, String organizationType, int enterpriseID) {
        this.organizationName = organizationName;
        this.organizationType = organizationType;
        this.enterpriseID = enterpriseID;
        this.createdDate = LocalDateTime.now();
    }
    
    public int getOrganizationID() { return organizationID; }
    public void setOrganizationID(int organizationID) { this.organizationID = organizationID; }
    
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    
    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }
    
    public int getEnterpriseID() { return enterpriseID; }
    public void setEnterpriseID(int enterpriseID) { this.enterpriseID = enterpriseID; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public Enterprise getEnterprise() { return enterprise; }
    public void setEnterprise(Enterprise enterprise) { this.enterprise = enterprise; }
    
    @Override
    public String toString() {
        return "Organization{organizationID=" + organizationID + ", name='" + organizationName + "', type='" + organizationType + "'}";
    }
}
