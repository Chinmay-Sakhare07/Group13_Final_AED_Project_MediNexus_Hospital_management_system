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
 * Service for insurance claim operations
 */
public class InsuranceService {

    private InsuranceClaimDAO claimDAO;
    private TreatmentDAO treatmentDAO;
    private PatientDAO patientDAO;
    private NotificationDAO notificationDAO;

    public InsuranceService() {
        this.claimDAO = new InsuranceClaimDAO();
        this.treatmentDAO = new TreatmentDAO();
        this.patientDAO = new PatientDAO();
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Get all pending claims
     */
    public List<InsuranceClaim> getPendingClaims() {
        return claimDAO.getClaimsByStatus("SUBMITTED");
    }

    /**
     * Get claims under review
     */
    public List<InsuranceClaim> getClaimsUnderReview() {
        return claimDAO.getClaimsByStatus("UNDER_REVIEW");
    }

    /**
     * Get claims for current patient
     */
    public List<InsuranceClaim> getMyClaims() {
        Patient currentPatient = UserSession.getInstance().getCurrentPatient();

        if (currentPatient == null) {
            System.err.println("No patient in session");
            return null;
        }

        return claimDAO.getClaimsByPatientID(currentPatient.getPatientID());
    }

    /**
     * Process insurance claim
     */
    public boolean processClaim(int claimID, String status, Double approvedAmount,
            String rejectionReason) {
        Employee currentProcessor = UserSession.getInstance().getCurrentEmployee();

        if (currentProcessor == null) {
            System.err.println("No insurance processor in session");
            return false;
        }

        // Get claim details
        InsuranceClaim claim = claimDAO.getClaimByID(claimID);
        if (claim == null) {
            System.err.println("Claim not found");
            return false;
        }

        // Process claim
        boolean success = claimDAO.processClaim(
                claimID,
                status,
                approvedAmount,
                rejectionReason,
                currentProcessor.getEmployeeID()
        );

        if (success) {
            System.out.println("Claim CL" + claimID + " processed: " + status);

            // Notify patient
            Patient patient = patientDAO.getPatientByID(claim.getPatientID());
            if (patient != null) {
                String message = String.format(
                        "Insurance claim CL%d has been %s. %s",
                        claimID,
                        status.toLowerCase(),
                        status.equals("APPROVED")
                        ? "Approved amount: $" + approvedAmount
                        : "Reason: " + rejectionReason
                );

                createNotification(
                        patient.getUserID(),
                        message,
                        status.equals("APPROVED") ? "SUCCESS" : "WARNING",
                        "CLAIM",
                        claimID
                );
            }

            return true;
        }

        System.err.println("Failed to process claim");
        return false;
    }

    /**
     * Calculate claim coverage (80% coverage example)
     */
    public double calculateCoverage(double claimAmount) {
        double coveragePercentage = 0.80; // 80% coverage
        return claimAmount * coveragePercentage;
    }

    /**
     * Get claim details
     */
    public InsuranceClaim getClaimDetails(int claimID) {
        InsuranceClaim claim = claimDAO.getClaimByID(claimID);

        if (claim != null) {
            // Load related data
            Patient patient = patientDAO.getPatientByID(claim.getPatientID());
            claim.setPatient(patient);

            Treatment treatment = treatmentDAO.getTreatmentByID(claim.getTreatmentID());
            claim.setTreatment(treatment);
        }

        return claim;
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
