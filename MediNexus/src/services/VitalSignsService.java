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
 * Service for vital signs management
 */
public class VitalSignsService {

    private VitalSignsDAO vitalSignsDAO;
    private ComplaintDAO complaintDAO;
    private PatientDAO patientDAO;
    private NotificationDAO notificationDAO;

    public VitalSignsService() {
        this.vitalSignsDAO = new VitalSignsDAO();
        this.complaintDAO = new ComplaintDAO();
        this.patientDAO = new PatientDAO();
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Record vital signs for a complaint
     */
    public VitalSigns recordVitalSigns(int complaintID, int patientID, Integer bpSystolic,
            Integer bpDiastolic, Integer heartRate, Double temperature,
            Integer respiratoryRate, Integer oxygenSaturation, Double weight) {
        Employee currentNurse = UserSession.getInstance().getCurrentEmployee();

        // Create vital signs record
        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setComplaintID(complaintID);
        vitalSigns.setPatientID(patientID);
        vitalSigns.setBloodPressureSystolic(bpSystolic);
        vitalSigns.setBloodPressureDiastolic(bpDiastolic);
        vitalSigns.setHeartRate(heartRate);
        vitalSigns.setTemperature(temperature);
        vitalSigns.setRespiratoryRate(respiratoryRate);
        vitalSigns.setOxygenSaturation(oxygenSaturation);
        vitalSigns.setWeight(weight);

        if (currentNurse != null) {
            vitalSigns.setRecordedBy(currentNurse.getEmployeeID());
        }

        boolean success = vitalSignsDAO.createVitalSigns(vitalSigns);

        if (success) {
            System.out.println("Vital signs recorded: V" + vitalSigns.getVitalSignID());

            // Check for critical values and notify
            checkCriticalVitals(vitalSigns, complaintID, patientID);

            return vitalSigns;
        }

        System.err.println("Failed to record vital signs");
        return null;
    }

    /**
     * Get vital signs for a complaint
     */
    public List<VitalSigns> getVitalSignsByComplaint(int complaintID) {
        return vitalSignsDAO.getVitalSignsByComplaintID(complaintID);
    }

    /**
     * Get vital signs for a patient
     */
    public List<VitalSigns> getVitalSignsByPatient(int patientID) {
        return vitalSignsDAO.getVitalSignsByPatientID(patientID);
    }

    /**
     * Get latest vital signs for a patient
     */
    public VitalSigns getLatestVitalSigns(int patientID) {
        List<VitalSigns> vitalsList = vitalSignsDAO.getVitalSignsByPatientID(patientID);

        if (!vitalsList.isEmpty()) {
            return vitalsList.get(0); // Most recent (ordered by date DESC)
        }

        return null;
    }

    /**
     * Check for critical vital signs and create alerts
     */
    private void checkCriticalVitals(VitalSigns vitals, int complaintID, int patientID) {
        boolean isCritical = false;
        StringBuilder alertMessage = new StringBuilder("CRITICAL VITALS ALERT: ");

        // Check blood pressure
        if (vitals.getBloodPressureSystolic() != null && vitals.getBloodPressureSystolic() > 140) {
            isCritical = true;
            alertMessage.append("High BP (").append(vitals.getBloodPressure()).append(") ");
        }

        // Check heart rate
        if (vitals.getHeartRate() != null && (vitals.getHeartRate() > 100 || vitals.getHeartRate() < 60)) {
            isCritical = true;
            alertMessage.append("Abnormal HR (").append(vitals.getHeartRate()).append(") ");
        }

        // Check oxygen saturation
        if (vitals.getOxygenSaturation() != null && vitals.getOxygenSaturation() < 95) {
            isCritical = true;
            alertMessage.append("Low SpO2 (").append(vitals.getOxygenSaturation()).append("%) ");
        }

        // Check temperature
        if (vitals.getTemperature() != null && (vitals.getTemperature() > 101.0 || vitals.getTemperature() < 95.0)) {
            isCritical = true;
            alertMessage.append("Abnormal Temp (").append(vitals.getTemperature()).append("°F) ");
        }

        // If critical, notify assigned doctor
        if (isCritical) {
            Complaint complaint = complaintDAO.getComplaintByID(complaintID);
            if (complaint != null && complaint.getAssignedDoctorID() != null) {
                Employee doctor = new EmployeeDAO().getEmployeeByID(complaint.getAssignedDoctorID());
                if (doctor != null) {
                    createNotification(
                            doctor.getUserID(),
                            alertMessage.toString() + "for complaint C" + complaintID,
                            "ALERT",
                            "VITALS",
                            vitals.getVitalSignID()
                    );
                }
            }
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
