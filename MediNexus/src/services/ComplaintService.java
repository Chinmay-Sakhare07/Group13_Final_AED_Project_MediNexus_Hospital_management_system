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
 * Service for managing patient complaints
 */
public class ComplaintService {

    private ComplaintDAO complaintDAO;
    private PatientDAO patientDAO;
    private EmployeeDAO employeeDAO;
    private NotificationDAO notificationDAO;

    public ComplaintService() {
        this.complaintDAO = new ComplaintDAO();
        this.patientDAO = new PatientDAO();
        this.employeeDAO = new EmployeeDAO();
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Submit a new complaint
     *
     * @param category Complaint category
     * @param priority Priority level
     * @param description Complaint description
     * @return Complaint object if successful, null otherwise
     */
    public Complaint submitComplaint(Complaint complaint) {
        // 1. Get current patient from session (Security Check)
        Patient currentPatient = UserSession.getInstance().getCurrentPatient();

        if (currentPatient == null) {
            System.err.println("Error: No patient found in session.");
            return null;
        }

        // 2. Set Patient ID (Ensure it comes from the logged-in user, not the form)
        complaint.setPatientID(currentPatient.getPatientID());

        // 3. Check for Doctor Assignment and Set Status
        // We check if the ID is not null and greater than 0 (valid ID)
        if (complaint.getAssignedDoctorID() != null && complaint.getAssignedDoctorID() > 0) {
            complaint.setStatus("ASSIGNED");
            System.out.println("DEBUG Service: Assigning to Doctor ID: " + complaint.getAssignedDoctorID());
        } else {
            complaint.setStatus("SUBMITTED"); // Default status for Triage
            complaint.setAssignedDoctorID(null); // Ensure it is explicitly null if no doctor selected
            System.out.println("DEBUG Service: No Doctor selected. Sent to Triage Queue.");
        }

        // 4. Save to Database via DAO
        // The DAO will look at the 'assignedDoctorID' field in the complaint object we just prepared
        boolean success = complaintDAO.createComplaint(complaint);

        if (success) {
            System.out.println("Complaint persisted successfully: C" + complaint.getComplaintID());

            // 5. Create Notification for Patient
            createNotification(
                    currentPatient.getUserID(),
                    "Your complaint C" + complaint.getComplaintID() + " has been submitted successfully.",
                    "SUCCESS",
                    "COMPLAINT",
                    complaint.getComplaintID()
            );

            // 6. Create Notification for Doctor (if one was assigned)
            if (complaint.getAssignedDoctorID() != null) {
                Employee doctor = employeeDAO.getEmployeeByID(complaint.getAssignedDoctorID());
                if (doctor != null) {
                    createNotification(
                            doctor.getUserID(),
                            "New complaint C" + complaint.getComplaintID() + " has been assigned to you (Priority: " + complaint.getPriority() + ")",
                            "ALERT",
                            "COMPLAINT",
                            complaint.getComplaintID()
                    );
                }
            }

            return complaint;
        }

        System.err.println("Failed to submit complaint to Database.");
        return null;
    }

    /**
     * Get complaints for current patient
     */
    public List<Complaint> getMyComplaints() {
        Patient currentPatient = UserSession.getInstance().getCurrentPatient();

        if (currentPatient == null) {
            System.err.println("No patient in session");
            return null;
        }

        return complaintDAO.getComplaintsByPatientID(currentPatient.getPatientID());
    }

    /**
     * Get complaints assigned to current doctor
     */
    public List<Complaint> getAssignedComplaints() {
        Employee currentEmployee = UserSession.getInstance().getCurrentEmployee();

        if (currentEmployee == null) {
            System.err.println("No employee in session");
            return null;
        }

        return complaintDAO.getComplaintsByDoctorID(currentEmployee.getEmployeeID());
    }

    /**
     * Assign complaint to a doctor
     */
    public boolean assignComplaintToDoctor(int complaintID, int doctorID) {
        Complaint complaint = complaintDAO.getComplaintByID(complaintID);

        if (complaint == null) {
            System.err.println("Complaint not found");
            return false;
        }

        boolean success = complaintDAO.assignComplaintToDoctor(complaintID, doctorID);

        if (success) {
            System.out.println("Complaint C" + complaintID + " assigned to doctor " + doctorID);

            // Notify patient
            Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
            if (patient != null) {
                Employee doctor = employeeDAO.getEmployeeByID(doctorID);
                createNotification(
                        patient.getUserID(),
                        "Your complaint C" + complaintID + " has been assigned to Dr. " + doctor.getFullName(),
                        "INFO",
                        "COMPLAINT",
                        complaintID
                );
            }

            // Notify doctor
            Employee doctor = employeeDAO.getEmployeeByID(doctorID);
            if (doctor != null) {
                createNotification(
                        doctor.getUserID(),
                        "New complaint C" + complaintID + " has been assigned to you (Priority: " + complaint.getPriority() + ")",
                        "ALERT",
                        "COMPLAINT",
                        complaintID
                );
            }

            return true;
        }

        System.err.println("Failed to assign complaint");
        return false;
    }

    /**
     * Update complaint status
     */
    public boolean updateComplaintStatus(int complaintID, String newStatus) {
        boolean success = complaintDAO.updateComplaintStatus(complaintID, newStatus);

        if (success) {
            System.out.println("Complaint C" + complaintID + " status updated to: " + newStatus);

            // Notify patient
            Complaint complaint = complaintDAO.getComplaintByID(complaintID);
            if (complaint != null) {
                Patient patient = patientDAO.getPatientByID(complaint.getPatientID());
                if (patient != null) {
                    createNotification(
                            patient.getUserID(),
                            "Complaint C" + complaintID + " status updated to: " + newStatus,
                            "INFO",
                            "COMPLAINT",
                            complaintID
                    );
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Get complaint by ID with full details
     */
    public Complaint getComplaintDetails(int complaintID) {
        return complaintDAO.getComplaintByID(complaintID);
    }

    /**
     * Get unassigned complaints (for doctors to pick up)
     */
    public List<Complaint> getUnassignedComplaints() {
        return complaintDAO.getUnassignedComplaints();
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
            System.err.println("Failed to create notification: " + e.getMessage());
        }
    }
}
