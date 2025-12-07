/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Chinmay
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date operations and formatting
 */
public class DateUtil {
    
    // Common date formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    
    /**
     * Format LocalDate to string (MM/dd/yyyy)
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string (MM/dd/yyyy hh:mm AM/PM)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Format time only (hh:mm AM/PM)
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMATTER);
    }
    
    /**
     * Get current date as formatted string
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * Get current datetime as formatted string
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * Calculate age from date of birth
     */
    public static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }
    
    /**
     * Get relative time string (e.g., "2 hours ago", "Just now")
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (minutes < 1440) { // Less than 24 hours
            long hours = minutes / 60;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes < 43200) { // Less than 30 days
            long days = minutes / 1440;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return formatDateTime(dateTime);
        }
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * Check if datetime is within last N hours
     */
    public static boolean isWithinLastHours(LocalDateTime dateTime, int hours) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        return dateTime.isAfter(cutoff);
    }
    
    /**
     * Test date utility methods
     */
    public static void main(String[] args) {
        System.out.println("=== Date Utility Test ===\n");
        
        // Current date/time
        System.out.println("CURRENT DATE/TIME:");
        System.out.println("Current date: " + getCurrentDate());
        System.out.println("Current datetime: " + getCurrentDateTime());
        
        // Age calculation
        System.out.println("\nAGE CALCULATION:");
        LocalDate dob = LocalDate.of(2000, 5, 15);
        System.out.println("Date of birth: " + formatDate(dob));
        System.out.println("Age: " + calculateAge(dob) + " years");
        
        // Relative time
        System.out.println("\nRELATIVE TIME:");
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        System.out.println("2 hours ago: " + getRelativeTime(twoHoursAgo));
        
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        System.out.println("Yesterday: " + getRelativeTime(yesterday));
        
        LocalDateTime justNow = LocalDateTime.now();
        System.out.println("Just now: " + getRelativeTime(justNow));
        
        // Date checks
        System.out.println("\nDATE CHECKS:");
        System.out.println("Is today today? " + (isToday(LocalDate.now()) ? "YES" : "NO"));
        System.out.println("Is yesterday today? " + (isToday(LocalDate.now().minusDays(1)) ? "YES" : "NO"));
        
        System.out.println("\nAll date tests completed!");
    }
}