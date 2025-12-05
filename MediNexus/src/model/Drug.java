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

public class Drug {
    private int drugID;
    private String drugName;
    private String genericName;
    private String category;
    private String manufacturer;
    private String dosageForm;
    private String strength;
    private Double unitPrice;
    private int stockQuantity;
    private String description;
    private String sideEffects;
    private LocalDateTime createdDate;
    
    public Drug() {
        this.stockQuantity = 0;
        this.createdDate = LocalDateTime.now();
    }
    
    public Drug(String drugName, String category, String dosageForm, String strength, Double unitPrice) {
        this.drugName = drugName;
        this.category = category;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.unitPrice = unitPrice;
        this.stockQuantity = 0;
        this.createdDate = LocalDateTime.now();
    }
    
    public int getDrugID() { return drugID; }
    public void setDrugID(int drugID) { this.drugID = drugID; }
    
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSideEffects() { return sideEffects; }
    public void setSideEffects(String sideEffects) { this.sideEffects = sideEffects; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public String getFullDrugInfo() { return drugName + " " + strength + " " + dosageForm; }
    
    @Override
    public String toString() {
        return "Drug{drugID=" + drugID + ", name='" + drugName + "', strength='" + strength + "', stock=" + stockQuantity + "}";
    }
}