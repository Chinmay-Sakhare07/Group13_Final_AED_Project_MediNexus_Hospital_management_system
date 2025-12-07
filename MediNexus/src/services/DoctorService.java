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
import java.time.LocalDate;
import java.util.List;

/**
 * Service for doctor operations
 */
public class DoctorService {

    private AssessmentDAO assessmentDAO;
    private DiagnosisDAO diagnosisDAO;
    private TreatmentDAO treatmentDAO;
    private ComplaintDAO complaintDAO;
    private SpecialistReferralDAO referralDAO;
    private NotificationDAO notificationDAO;
    private PatientDAO patientDAO;
    private EmployeeDAO employeeDAO;

    public DoctorService() {
        this.assessmentDAO = new AssessmentDAO();
        this.diagnosisDAO = new DiagnosisDAO();
        this.treatmentDAO = new TreatmentDAO();
        this.complaintDAO = new ComplaintDAO();
        this.referralDAO = new SpecialistReferralDAO();
        this.notificationDAO = new NotificationDAO();
        this.patientDAO = new PatientDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    /**
     * Create assessment for a complaint
     */
    public Assessment createAssessment(int complaintID, String symptoms,
            String physicalExam, String notes) {
        Employee currentDoctor = UserSession.getInstance().getCurrentEmployee();

        if (currentDoctor == null) {
            System.err.println("No doctor in session");
            return null;
        }

        // Create assessment
        Assessment assessment = new Assessment();
        assessment.setComplaintID(complaintID);
        assessment.setDoctorID(currentDoctor.getEmployeeID());
        assessment.setSymptoms(symptoms);
        assessment.setPhysicalExamination(physicalExam);
        assessment.setPreliminaryNotes(notes);

        boolean success = assessmentDAO.createAssessment(assessment);

        if (success) {
            // Update complaint status
            complaintDAO.updateComplaintStatus(complaintID, "IN_PROGRESS");

            System.out.println("Assessment created: A" + assessment.getAssessmentID());

            // Notify patient
            Complaint complaint = complaintDAO.getComplaintByID(complaintID);
            if (complaint != null) {
                Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
                if (patient != null) {
                    createNotification(
                            patient.getUserID(),
                            "Dr. " + currentDoctor.getFullName() + " has assessed your complaint C" + complaintID,
                            "INFO",
                            "COMPLAINT",
                            complaintID
                    );
                }
            }

            return assessment;
        }

        System.err.println("Failed to create assessment");
        return null;
    }

    /**
     * Create diagnosis
     */
    public Diagnosis createDiagnosis(int assessmentID, String diagnosisCode,
            String diagnosisName, String description, String severity) {
        Employee currentDoctor = UserSession.getInstance().getCurrentEmployee();

        if (currentDoctor == null) {
            System.err.println("No doctor in session");
            return null;
        }

        // Create diagnosis
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setAssessmentID(assessmentID);
        diagnosis.setDiagnosisCode(diagnosisCode);
        diagnosis.setDiagnosisName(diagnosisName);
        diagnosis.setDescription(description);
        diagnosis.setSeverity(severity);
        diagnosis.setDiagnosedBy(currentDoctor.getEmployeeID());

        boolean success = diagnosisDAO.createDiagnosis(diagnosis);

        if (success) {
            // Update complaint status
            Assessment assessment = assessmentDAO.getAssessmentByID(assessmentID);
            if (assessment != null) {
                complaintDAO.updateComplaintStatus(assessment.getComplaintID(), "DIAGNOSED");

                // Notify patient
                Complaint complaint = complaintDAO.getComplaintByID(assessment.getComplaintID());
                if (complaint != null) {
                    Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
                    if (patient != null) {
                        createNotification(
                                patient.getUserID(),
                                "Your diagnosis is ready: " + diagnosisName,
                                "INFO",
                                "COMPLAINT",
                                complaint.getComplaintID()
                        );
                    }
                }
            }

            System.out.println("Diagnosis created: D" + diagnosis.getDiagnosisID());
            return diagnosis;
        }

        System.err.println("Failed to create diagnosis");
        return null;
    }

    /**
     * Create treatment plan
     */
    public Treatment createTreatment(int diagnosisID, String treatmentPlan,
            String medicationsPrescribed, String instructions,
            LocalDate followUpDate) {
        Employee currentDoctor = UserSession.getInstance().getCurrentEmployee();

        if (currentDoctor == null) {
            System.err.println("No doctor in session");
            return null;
        }

        // Create treatment
        Treatment treatment = new Treatment();
        treatment.setDiagnosisID(diagnosisID);
        treatment.setTreatmentPlan(treatmentPlan);
        treatment.setMedicationsPrescribed(medicationsPrescribed);
        treatment.setInstructions(instructions);
        treatment.setFollowUpDate(followUpDate);
        treatment.setPrescribedBy(currentDoctor.getEmployeeID());

        boolean success = treatmentDAO.createTreatment(treatment);

        if (success) {
            // Update complaint status to TREATED
            Diagnosis diagnosis = diagnosisDAO.getDiagnosisByID(diagnosisID);
            if (diagnosis != null) {
                Assessment assessment = assessmentDAO.getAssessmentByID(diagnosis.getAssessmentID());
                if (assessment != null) {
                    complaintDAO.updateComplaintStatus(assessment.getComplaintID(), "TREATED");
                }
            }

            System.out.println("Treatment created: T" + treatment.getTreatmentID());
            return treatment;
        }

        System.err.println("Failed to create treatment");
        return null;
    }

    /**
     * Refer to specialist
     */
    public SpecialistReferral referToSpecialist(int diagnosisID, String referralReason,
            Integer specialistID) {
        Employee currentDoctor = UserSession.getInstance().getCurrentEmployee();

        if (currentDoctor == null) {
            System.err.println("No doctor in session");
            return null;
        }

        // Create referral
        SpecialistReferral referral = new SpecialistReferral();
        referral.setDiagnosisID(diagnosisID);
        referral.setReferredBy(currentDoctor.getEmployeeID());
        referral.setReferredTo(specialistID);
        referral.setReferralReason(referralReason);
        referral.setStatus("PENDING");

        boolean success = referralDAO.createReferral(referral);

        if (success) {
            System.out.println("Referral created: R" + referral.getReferralID());

            // Notify specialist if assigned
            if (specialistID != null) {
                Employee specialist = employeeDAO.getEmployeeByID(specialistID);
                if (specialist != null) {
                    createNotification(
                            specialist.getUserID(),
                            "New referral R" + referral.getReferralID() + " from Dr. " + currentDoctor.getFullName(),
                            "ALERT",
                            "REFERRAL",
                            referral.getReferralID()
                    );
                }
            }

            return referral;
        }

        System.err.println("Failed to create referral");
        return null;
    }

    /**
     * Get assessment for a complaint
     */
    public Assessment getAssessmentByComplaintID(int complaintID) {
        return assessmentDAO.getAssessmentByComplaintID(complaintID);
    }

    /**
     * Get diagnosis for an assessment
     */
    public Diagnosis getDiagnosisByAssessmentID(int assessmentID) {
        return diagnosisDAO.getDiagnosisByAssessmentID(assessmentID);
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
