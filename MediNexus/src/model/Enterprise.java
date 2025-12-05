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

public class Enterprise {
    private int enterpriseID;
    private String enterpriseName;
    private String enterpriseType;
    private LocalDateTime createdDate;
    
    public Enterprise() {
        this.createdDate = LocalDateTime.now();
    }
    
    public Enterprise(String enterpriseName, String enterpriseType) {
        this.enterpriseName = enterpriseName;
        this.enterpriseType = enterpriseType;
        this.createdDate = LocalDateTime.now();
    }
    
    public int getEnterpriseID() { return enterpriseID; }
    public void setEnterpriseID(int enterpriseID) { this.enterpriseID = enterpriseID; }
    
    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }
    
    public String getEnterpriseType() { return enterpriseType; }
    public void setEnterpriseType(String enterpriseType) { this.enterpriseType = enterpriseType; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "Enterprise{enterpriseID=" + enterpriseID + ", name='" + enterpriseName + "', type='" + enterpriseType + "'}";
    }
}
