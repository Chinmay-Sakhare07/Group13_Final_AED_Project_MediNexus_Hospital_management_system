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
 * Service for specialist operations
 */
public class SpecialistService {

    private SpecialistReferralDAO referralDAO;
    private DiagnosisDAO diagnosisDAO;
    private AssessmentDAO assessmentDAO;
    private ComplaintDAO complaintDAO;
    private NotificationDAO notificationDAO;
    private EmployeeDAO employeeDAO;
    private PatientDAO patientDAO;

    public SpecialistService() {
        this.referralDAO = new SpecialistReferralDAO();
        this.diagnosisDAO = new DiagnosisDAO();
        this.assessmentDAO = new AssessmentDAO();
        this.complaintDAO = new ComplaintDAO();
        this.notificationDAO = new NotificationDAO();
        this.employeeDAO = new EmployeeDAO();
        this.patientDAO = new PatientDAO();
    }

    /**
     * Get referrals for current specialist
     */
    public List<SpecialistReferral> getMyReferrals() {
        Employee currentSpecialist = UserSession.getInstance().getCurrentEmployee();

        if (currentSpecialist == null) {
            System.err.println("No specialist in session");
            return null;
        }

        return referralDAO.getReferralsBySpecialistID(currentSpecialist.getEmployeeID());
    }

    /**
     * Get pending referrals (not yet assigned to specific specialist)
     */
    public List<SpecialistReferral> getPendingReferrals() {
        return referralDAO.getReferralsByStatus("PENDING");
    }

    /**
     * Accept a referral
     */
    public boolean acceptReferral(int referralID) {
        Employee currentSpecialist = UserSession.getInstance().getCurrentEmployee();

        if (currentSpecialist == null) {
            System.err.println("No specialist in session");
            return false;
        }

        boolean success = referralDAO.assignReferralToSpecialist(referralID, currentSpecialist.getEmployeeID());

        if (success) {
            System.out.println("Referral R" + referralID + " accepted");

            // Notify referring doctor
            SpecialistReferral referral = referralDAO.getReferralByID(referralID);
            if (referral != null) {
                Employee referringDoctor = employeeDAO.getEmployeeByID(referral.getReferredBy());
                if (referringDoctor != null) {
                    createNotification(
                            referringDoctor.getUserID(),
                            "Dr. " + currentSpecialist.getFullName() + " has accepted referral R" + referralID,
                            "INFO",
                            "REFERRAL",
                            referralID
                    );
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Provide specialist consultation
     */
    public boolean provideConsultation(int referralID, String specialistNotes, String status) {
        Employee currentSpecialist = UserSession.getInstance().getCurrentEmployee();

        if (currentSpecialist == null) {
            System.err.println("No specialist in session");
            return false;
        }

        boolean success = referralDAO.updateReferralStatus(referralID, status, specialistNotes);

        if (success) {
            System.out.println("Consultation provided for referral R" + referralID);

            // Get referral details
            SpecialistReferral referral = referralDAO.getReferralByID(referralID);
            if (referral != null) {
                // Notify referring doctor
                Employee referringDoctor = employeeDAO.getEmployeeByID(referral.getReferredBy());
                if (referringDoctor != null) {
                    createNotification(
                            referringDoctor.getUserID(),
                            "Specialist consultation completed for referral R" + referralID,
                            "SUCCESS",
                            "REFERRAL",
                            referralID
                    );
                }

                // Notify patient
                Diagnosis diagnosis = diagnosisDAO.getDiagnosisByID(referral.getDiagnosisID());
                if (diagnosis != null) {
                    Assessment assessment = assessmentDAO.getAssessmentByID(diagnosis.getAssessmentID());
                    if (assessment != null) {
                        Complaint complaint = complaintDAO.getComplaintByID(assessment.getComplaintID());
                        if (complaint != null) {
                            Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
                            if (patient != null) {
                                createNotification(
                                        patient.getUserID(),
                                        "Specialist consultation completed for your complaint",
                                        "INFO",
                                        "COMPLAINT",
                                        complaint.getComplaintID()
                                );
                            }
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Get referral details
     */
    public SpecialistReferral getReferralDetails(int referralID) {
        return referralDAO.getReferralByID(referralID);
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
