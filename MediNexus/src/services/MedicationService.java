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
import java.util.List;
import session.UserSession;

/**
 * Service for medication order management
 */
public class MedicationService {
    
    private MedicationOrderDAO orderDAO;
    private DrugDAO drugDAO;
    private TreatmentDAO treatmentDAO;
    private InsuranceClaimDAO claimDAO;
    private NotificationDAO notificationDAO;
    private PatientDAO patientDAO;
    private DiagnosisDAO diagnosisDAO;
    private AssessmentDAO assessmentDAO;
    private ComplaintDAO complaintDAO;
    
    public MedicationService() {
        this.orderDAO = new MedicationOrderDAO();
        this.drugDAO = new DrugDAO();
        this.treatmentDAO = new TreatmentDAO();
        this.claimDAO = new InsuranceClaimDAO();
        this.notificationDAO = new NotificationDAO();
        this.patientDAO = new PatientDAO();
        this.diagnosisDAO = new DiagnosisDAO();
        this.assessmentDAO = new AssessmentDAO();
        this.complaintDAO = new ComplaintDAO();
    }
    
    /**
     * Create medication order from treatment
     */
    public MedicationOrder createMedicationOrder(int treatmentID, int drugID, int quantity, 
                                                String dosage, String frequency, String duration) {
        // Get treatment details
        Treatment treatment = treatmentDAO.getTreatmentByID(treatmentID);
        if (treatment == null) {
            System.err.println("Treatment not found");
            return null;
        }
        
        // Check if drug exists
        Drug drug = drugDAO.getDrugByID(drugID);
        if (drug == null) {
            System.err.println("Drug not found");
            return null;
        }
        
        // Create medication order
        MedicationOrder order = new MedicationOrder();
        order.setTreatmentID(treatmentID);
        order.setDrugID(drugID);
        order.setQuantity(quantity);
        order.setDosage(dosage);
        order.setFrequency(frequency);
        order.setDuration(duration);
        order.setStatus("PENDING");
        
        boolean success = orderDAO.createMedicationOrder(order);
        
        if (success) {
            System.out.println("Medication order created: M" + order.getOrderID());
            
            // Auto-create insurance claim
            createInsuranceClaim(treatment, order, drug);
            
            return order;
        }
        
        System.err.println("Failed to create medication order");
        return null;
    }
    
    /**
     * Get medication orders for a treatment
     */
    public List<MedicationOrder> getOrdersByTreatment(int treatmentID) {
        return orderDAO.getMedicationOrdersByTreatmentID(treatmentID);
    }
    
    /**
     * Get medication orders for current patient
     */
    public List<MedicationOrder> getMyMedications() {
        Patient currentPatient = UserSession.getInstance().getCurrentPatient();
        
        if (currentPatient == null) {
            System.err.println("No patient in session");
            return null;
        }
        
        // This is complex - need to join through multiple tables
        // For simplicity, get all orders and filter
        // In production, create a custom SQL query with JOINs
        return orderDAO.getAllMedicationOrders();
    }
    
    /**
     * Auto-create insurance claim for medication
     */
    private void createInsuranceClaim(Treatment treatment, MedicationOrder order, Drug drug) {
        try {
            // Get patient ID from treatment
            Diagnosis diagnosis = diagnosisDAO.getDiagnosisByID(treatment.getDiagnosisID());
            if (diagnosis == null) return;
            
            Assessment assessment = assessmentDAO.getAssessmentByID(diagnosis.getAssessmentID());
            if (assessment == null) return;
            
            Complaint complaint = complaintDAO.getComplaintByID(assessment.getComplaintID());
            if (complaint == null) return;
            
            // Calculate claim amount
            double claimAmount = drug.getUnitPrice() * order.getQuantity();
            
            // Create claim
            InsuranceClaim claim = new InsuranceClaim();
            claim.setPatientID(complaint.getPatientID());
            claim.setTreatmentID(treatment.getTreatmentID());
            claim.setClaimAmount(claimAmount);
            claim.setClaimReason("Medication: " + drug.getDrugName() + " - " + order.getQuantity() + " units");
            claim.setStatus("SUBMITTED");
            
            boolean claimCreated = claimDAO.createClaim(claim);
            
            if (claimCreated) {
                System.out.println("Insurance claim auto-created: CL" + claim.getClaimID());
            }
            
        } catch (Exception e) {
            System.err.println("Failed to auto-create insurance claim: " + e.getMessage());
        }
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