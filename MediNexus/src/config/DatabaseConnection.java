/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Chinmay
 */
public class DatabaseConnection {

    // ✅ SQL SERVER AUTHENTICATION (No DLL needed!)
    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "MediNexus";
    private static final String USERNAME = "medinexus_user";
    private static final String PASSWORD = "MediNexus25";

    // Connection URL with SQL Authentication
    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT + ";"
            + "databaseName=" + DATABASE_NAME + ";"
            + "user=" + USERNAME + ";"
            + "password=" + PASSWORD + ";"
            + "encrypt=false;";

    private static Connection connection = null;

    /**
     * Get database connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load SQL Server JDBC driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Establish connection
                connection = DriverManager.getConnection(URL);

                System.out.println("========================================");
                System.out.println("DATABASE CONNECTED SUCCESSFULLY!");
                System.out.println("========================================");
                System.out.println("Server: " + SERVER_NAME + ":" + PORT);
                System.out.println("Database: " + DATABASE_NAME);
                System.out.println("User: " + USERNAME);
                System.out.println("Authentication: SQL Server");
                System.out.println("Status: CONNECTED");
                System.out.println("========================================");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("========================================");
            System.err.println("JDBC DRIVER NOT FOUND!");
            System.err.println("========================================");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("CONNECTION FAILED!");
            System.err.println("========================================");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection");
            e.printStackTrace();
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Main method to test connection
     */
    public static void main(String[] args) {
        System.out.println("\n===========================================");
        System.out.println("  TESTING SQL SERVER DATABASE CONNECTION");
        System.out.println("===========================================\n");

        System.out.println("Connection Details:");
        System.out.println("- Server: " + SERVER_NAME + ":" + PORT);
        System.out.println("- Database: " + DATABASE_NAME);
        System.out.println("- Authentication: SQL Server Authentication");
        System.out.println("- User: " + USERNAME);
        System.out.println("\nAttempting connection...\n");

        if (testConnection()) {
            System.out.println("\n===========================================");
            System.out.println("CONNECTION TEST: SUCCESS!");
            System.out.println("===========================================");
        } else {
            System.out.println("\n===========================================");
            System.out.println("CONNECTION TEST: FAILED");
            System.out.println("===========================================");

            closeConnection();
        }
    }
}
