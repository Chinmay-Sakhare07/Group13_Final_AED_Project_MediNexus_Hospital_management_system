/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Chinmay
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {
    
    /**
     * Hash a password using SHA-256
     * @param password Plain text password
     * @return Hashed password (64 characters)
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    /**
     * Verify if a password matches the hashed password
     * @param password Plain text password to verify
     * @param hashedPassword Hashed password from database
     * @return true if passwords match
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashOfInput = hashPassword(password);
        return hashOfInput.equals(hashedPassword);
    }
    
    /**
     * Test password hashing
     */
    public static void main(String[] args) {
        System.out.println("=== Password Utility Test ===\n");
        
        // Test hashing
        String password = "password123";
        String hashed = hashPassword(password);
        
        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password: " + hashed);
        System.out.println("Hash Length: " + hashed.length() + " characters\n");
        
        // Test verification
        boolean isValid = verifyPassword(password, hashed);
        System.out.println("Verification Test: " + (isValid ? "PASS" : "FAIL"));
        
        // Test wrong password
        boolean isInvalid = verifyPassword("wrongpassword", hashed);
        System.out.println("Wrong Password Test: " + (!isInvalid ? "PASS" : "FAIL"));
        
        System.out.println("\nAll tests completed!");
    }
}