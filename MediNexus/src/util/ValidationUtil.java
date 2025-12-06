/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Chinmay
 */
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    // Regex patterns for validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final String NAME_REGEX = "^[A-Za-z\\s]{2,50}$";
    
    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Check if string is empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }
    
    /**
     * Validate phone number (10 digits)
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return Pattern.compile(PHONE_REGEX).matcher(phone).matches();
    }
    
    /**
     * Validate name (letters and spaces only, 2-50 characters)
     */
    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }
        return Pattern.compile(NAME_REGEX).matcher(name).matches();
    }
    
    /**
     * Check if value is a positive number
     */
    public static boolean isPositiveNumber(String value) {
        if (isEmpty(value)) {
            return false;
        }
        try {
            double num = Double.parseDouble(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Check if value is a valid integer
     */
    public static boolean isValidInteger(String value) {
        if (isEmpty(value)) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Check if value is within range (inclusive)
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Check if value is within range (inclusive)
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * Test validation methods
     */
    public static void main(String[] args) {
        System.out.println("=== Validation Utility Test ===\n");
        
        // Email tests
        System.out.println("EMAIL VALIDATION:");
        System.out.println("'test@email.com' → " + (isValidEmail("test@email.com") ? "VALID" : "INVALID"));
        System.out.println("'invalid.email' → " + (isValidEmail("invalid.email") ? "VALID" : "INVALID"));
        
        // Phone tests
        System.out.println("\nPHONE VALIDATION:");
        System.out.println("'9876543210' → " + (isValidPhone("9876543210") ? "VALID" : "INVALID"));
        System.out.println("'123' → " + (isValidPhone("123") ? "VALID" : "INVALID"));
        
        // Name tests
        System.out.println("\nNAME VALIDATION:");
        System.out.println("'Sreesh Kulkarni' → " + (isValidName("Sreesh Kulkarni") ? "VALID" : "INVALID"));
        System.out.println("'Sreesh123' → " + (isValidName("Sreesh123") ? "VALID" : "INVALID"));
        
        // Number tests
        System.out.println("\nNUMBER VALIDATION:");
        System.out.println("'100' is positive → " + (isPositiveNumber("100") ? "YES" : "NO"));
        System.out.println("'-50' is positive → " + (isPositiveNumber("-50") ? "YES" : "NO"));
        
        // Range tests
        System.out.println("\nRANGE VALIDATION:");
        System.out.println("50 in range [0-100] → " + (isInRange(50, 0, 100) ? "YES" : "NO"));
        System.out.println("150 in range [0-100] → " + (isInRange(150, 0, 100) ? "YES" : "NO"));
        
        System.out.println("\nAll validation tests completed!");
    }
}