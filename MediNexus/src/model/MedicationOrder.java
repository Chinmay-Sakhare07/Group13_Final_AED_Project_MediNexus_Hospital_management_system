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

public class MedicationOrder {
    private int orderID;
    private int treatmentID;
    private int drugID;
    private int quantity;
    private String dosage;
    private String frequency;
    private String duration;
    private String status;
    private Integer pharmacistID;
    private LocalDateTime orderDate;
    private LocalDateTime fulfilledDate;
    private Treatment treatment;
    private Drug drug;
    private Employee pharmacist;
    
    public MedicationOrder() {
        this.status = "PENDING";
        this.orderDate = LocalDateTime.now();
    }
    
    public MedicationOrder(int treatmentID, int drugID, int quantity, String dosage) {
        this.treatmentID = treatmentID;
        this.drugID = drugID;
        this.quantity = quantity;
        this.dosage = dosage;
        this.status = "PENDING";
        this.orderDate = LocalDateTime.now();
    }
    
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    
    public int getTreatmentID() { return treatmentID; }
    public void setTreatmentID(int treatmentID) { this.treatmentID = treatmentID; }
    
    public int getDrugID() { return drugID; }
    public void setDrugID(int drugID) { this.drugID = drugID; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getPharmacistID() { return pharmacistID; }
    public void setPharmacistID(Integer pharmacistID) { this.pharmacistID = pharmacistID; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getFulfilledDate() { return fulfilledDate; }
    public void setFulfilledDate(LocalDateTime fulfilledDate) { this.fulfilledDate = fulfilledDate; }
    
    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }
    
    public Drug getDrug() { return drug; }
    public void setDrug(Drug drug) { this.drug = drug; }
    
    public Employee getPharmacist() { return pharmacist; }
    public void setPharmacist(Employee pharmacist) { this.pharmacist = pharmacist; }
    
    @Override
    public String toString() {
        return "MedicationOrder{orderID=" + orderID + ", drugID=" + drugID + ", quantity=" + quantity + ", status='" + status + "'}";
    }
}
