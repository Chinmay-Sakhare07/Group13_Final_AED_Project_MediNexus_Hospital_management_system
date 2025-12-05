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

public class Notification {
    private int notificationID;
    private int userID;
    private String message;
    private String notificationType;
    private boolean isRead;
    private String relatedEntityType;
    private Integer relatedEntityID;
    private LocalDateTime createdDate;
    private User user;
    
    public Notification() {
        this.isRead = false;
        this.createdDate = LocalDateTime.now();
    }
    
    public Notification(int userID, String message, String notificationType) {
        this.userID = userID;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = false;
        this.createdDate = LocalDateTime.now();
    }
    
    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }
    
    public Integer getRelatedEntityID() { return relatedEntityID; }
    public void setRelatedEntityID(Integer relatedEntityID) { this.relatedEntityID = relatedEntityID; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    @Override
    public String toString() {
        return "Notification{notificationID=" + notificationID + ", message='" + message + "', isRead=" + isRead + "}";
    }
}
