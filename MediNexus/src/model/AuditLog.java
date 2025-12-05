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

public class AuditLog {
    private int logID;
    private int userID;
    private String action;
    private String entityType;
    private Integer entityID;
    private String details;
    private String ipAddress;
    private LocalDateTime timestamp;
    private User user;
    
    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }
    
    public AuditLog(int userID, String action, String entityType, Integer entityID, String details) {
        this.userID = userID;
        this.action = action;
        this.entityType = entityType;
        this.entityID = entityID;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    public int getLogID() { return logID; }
    public void setLogID(int logID) { this.logID = logID; }
    
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    
    public Integer getEntityID() { return entityID; }
    public void setEntityID(Integer entityID) { this.entityID = entityID; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    @Override
    public String toString() {
        return "AuditLog{logID=" + logID + ", action='" + action + "', entityType='" + entityType + "', timestamp=" + timestamp + "}";
    }
}
