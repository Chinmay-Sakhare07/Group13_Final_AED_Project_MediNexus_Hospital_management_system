/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Chinmay
 */
import dao.NotificationDAO;
import model.Notification;
import session.UserSession;
import java.util.List;
import model.User;

/**
 * Service for notification management
 */
public class NotificationService {

    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Get notifications for current user
     */
    public List<Notification> getMyNotifications() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.err.println("No user in session");
            return null;
        }

        return notificationDAO.getNotificationsByUserID(currentUser.getUserID());
    }

    /**
     * Get unread notifications for current user
     */
    public List<Notification> getUnreadNotifications() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.err.println("No user in session");
            return null;
        }

        return notificationDAO.getUnreadNotificationsByUserID(currentUser.getUserID());
    }

    /**
     * Get unread notification count
     */
    public int getUnreadCount() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            return 0;
        }

        return notificationDAO.getUnreadNotificationCount(currentUser.getUserID());
    }

    /**
     * Mark notification as read
     */
    public boolean markAsRead(int notificationID) {
        return notificationDAO.markAsRead(notificationID);
    }

    /**
     * Mark all notifications as read for current user
     */
    public boolean markAllAsRead() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.err.println("No user in session");
            return false;
        }

        return notificationDAO.markAllAsReadForUser(currentUser.getUserID());
    }

    /**
     * Create a new notification
     */
    public boolean createNotification(int userID, String message, String type,
            String entityType, Integer entityID) {
        Notification notification = new Notification();
        notification.setUserID(userID);
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setRelatedEntityType(entityType);
        notification.setRelatedEntityID(entityID);

        return notificationDAO.createNotification(notification);
    }

    /**
     * Delete notification
     */
    public boolean deleteNotification(int notificationID) {
        return notificationDAO.deleteNotification(notificationID);
    }

    /**
     * Clear all notifications for current user
     */
    public boolean clearAllNotifications() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.err.println("No user in session");
            return false;
        }

        return notificationDAO.deleteAllNotificationsForUser(currentUser.getUserID());
    }
}
