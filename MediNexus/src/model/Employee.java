/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP
 */

public class Employee {
    private int employeeID;
    private int userID;
    private String firstName;
    private String lastName;
    private String specialization;
    private String licenseNumber;
    private int organizationID;
    private User user;
    private Organization organization;
    
    public Employee() {}
    
    public Employee(int userID, String firstName, String lastName, String specialization, 
                    String licenseNumber, int organizationID) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.organizationID = organizationID;
    }
    
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return firstName + " " + lastName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public int getOrganizationID() { return organizationID; }
    public void setOrganizationID(int organizationID) { this.organizationID = organizationID; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    
    @Override
    public String toString() {
        return "Employee{employeeID=" + employeeID + ", name='" + getFullName() + "', specialization='" + specialization + "'}";
    }

    
}
