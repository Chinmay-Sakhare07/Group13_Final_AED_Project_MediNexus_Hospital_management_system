/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Chinmay
 */
import dao.*;
import model.*;
import session.UserSession;
import java.util.List;

/**
 * Service for pharmacist operations
 */
public class PharmacistService {
    
    private MedicationOrderDAO orderDAO;
    private DrugDAO drugDAO;
    private TreatmentDAO treatmentDAO;
    private NotificationDAO notificationDAO;
    private PatientDAO patientDAO;
    private DiagnosisDAO diagnosisDAO;
    private AssessmentDAO assessmentDAO;
    private ComplaintDAO complaintDAO;
    
    public PharmacistService() {
        this.orderDAO = new MedicationOrderDAO();
        this.drugDAO = new DrugDAO();
        this.treatmentDAO = new TreatmentDAO();
        this.notificationDAO = new NotificationDAO();
        this.patientDAO = new PatientDAO();
        this.diagnosisDAO = new DiagnosisDAO();
        this.assessmentDAO = new AssessmentDAO();
        this.complaintDAO = new ComplaintDAO();
    }
    
    /**
     * Get pending medication orders
     */
    public List<MedicationOrder> getPendingOrders() {
        return orderDAO.getMedicationOrdersByStatus("PENDING");
    }
    
    /**
     * Get all medication orders
     */
    public List<MedicationOrder> getAllOrders() {
        return orderDAO.getAllMedicationOrders();
    }
    
    /**
     * Get medication order details with drug info
     */
    public MedicationOrder getOrderDetails(int orderID) {
        MedicationOrder order = orderDAO.getMedicationOrderByID(orderID);
        
        if (order != null) {
            // Load drug details
            Drug drug = drugDAO.getDrugByID(order.getDrugID());
            order.setDrug(drug);
        }
        
        return order;
    }
    
    /**
     * Check drug interactions
     * (Simplified version - in real system would use API)
     */
    public String checkDrugInteractions(int orderID) {
        // For now, return simple message
        // In production, you'd call an external API or check against interaction database
        return "No known interactions found";
    }
    
    /**
     * Fulfill medication order
     */
    public boolean fulfillOrder(int orderID, String status) {
        Employee currentPharmacist = UserSession.getInstance().getCurrentEmployee();
        
        if (currentPharmacist == null) {
            System.err.println("No pharmacist in session");
            return false;
        }
        
        // Get order details
        MedicationOrder order = orderDAO.getMedicationOrderByID(orderID);
        if (order == null) {
            System.err.println("Order not found");
            return false;
        }
        
        // Check stock
        Drug drug = drugDAO.getDrugByID(order.getDrugID());
        if (drug == null) {
            System.err.println("Drug not found");
            return false;
        }
        
        if (drug.getStockQuantity() < order.getQuantity()) {
            System.err.println("Insufficient stock. Available: " + drug.getStockQuantity() + 
                             ", Required: " + order.getQuantity());
            return false;
        }
        
        // Update order status
        boolean success = orderDAO.updateMedicationOrderStatus(
            orderID, 
            status, 
            currentPharmacist.getEmployeeID()
        );
        
        if (success) {
            // Reduce stock
            drugDAO.updateStock(drug.getDrugID(), -order.getQuantity());
            
            System.out.println("Order M" + orderID + " fulfilled");
            
            // Notify patient
            Treatment treatment = treatmentDAO.getTreatmentByID(order.getTreatmentID());
            if (treatment != null) {
                Diagnosis diagnosis = diagnosisDAO.getDiagnosisByID(treatment.getDiagnosisID());
                if (diagnosis != null) {
                    Assessment assessment = assessmentDAO.getAssessmentByID(diagnosis.getAssessmentID());
                    if (assessment != null) {
                        Complaint complaint = complaintDAO.getComplaintByID(assessment.getComplaintID());
                        if (complaint != null) {
                            Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
                            if (patient != null) {
                                createNotification(
                                    patient.getUserID(),
                                    "Your medication (Order M" + orderID + ") is " + 
                                    (status.equals("READY") ? "ready for pickup" : "being processed"),
                                    "INFO",
                                    "MEDICATION",
                                    orderID
                                );
                            }
                        }
                    }
                }
            }
            
            return true;
        }
        
        System.err.println("Failed to fulfill order");
        return false;
    }
    
    /**
     * Get low stock drugs
     */
    public List<Drug> getLowStockDrugs(int threshold) {
        return drugDAO.getLowStockDrugs(threshold);
    }
    
    /**
     * Search drugs
     */
    public List<Drug> searchDrugs(String searchTerm) {
        return drugDAO.searchDrugsByName(searchTerm);
    }
    
    /**
     * Helper method to create notification
     */
    private void createNotification(int userID, String message, String type, 
                                   String entityType, int entityID) {
        try {
            Notification notification = new Notification();
            notification.setUserID(userID);
            notification.setMessage(message);
            notification.setNotificationType(type);
            notification.setRelatedEntityType(entityType);
            notification.setRelatedEntityID(entityID);
            
            notificationDAO.createNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to create notification");
        }
    }
}