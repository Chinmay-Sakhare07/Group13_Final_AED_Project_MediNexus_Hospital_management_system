package services;

import dao.*;
import model.*;
import session.UserSession;
import util.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Comprehensive CRUD test for all services and DAOs
 */
public class TestServices {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  COMPREHENSIVE CRUD TEST");
        System.out.println("========================================\n");
        
        // Test 1: User CRUD
        testUserCRUD();
        
        // Test 2: Patient CRUD
        testPatientCRUD();
        
        // Test 3: Complaint CRUD
        testComplaintCRUD();
        
        // Test 4: Drug CRUD
        testDrugCRUD();
        
        // Test 5: Complete Workflow Test
        testCompleteWorkflow();
        
        System.out.println("\n========================================");
        System.out.println("  ✓ ALL CRUD TESTS COMPLETED!");
        System.out.println("========================================");
    }
    
    // ========================================
    // TEST 1: USER CRUD
    // ========================================
    private static void testUserCRUD() {
        System.out.println("TEST 1: USER CRUD OPERATIONS");
        System.out.println("========================================");
        
        UserDAO userDAO = new UserDAO();
        
        // CREATE
        System.out.println("\n[CREATE] Creating new user...");
        User newUser = new User();
        newUser.setUsername("testuser_" + System.currentTimeMillis());
        newUser.setPasswordHash(PasswordUtil.hashPassword("test123"));
        newUser.setRole("PATIENT");
        newUser.setEmail("testuser@test.com");
        newUser.setPhone("1234567890");
        newUser.setActive(true);
        
        boolean created = userDAO.createUser(newUser);
        if (created) {
            System.out.println("✓ User created successfully!");
            System.out.println("  UserID: " + newUser.getUserID());
            System.out.println("  Username: " + newUser.getUsername());
        } else {
            System.out.println("✗ Failed to create user");
            return;
        }
        
        // READ
        System.out.println("\n[READ] Reading user by ID...");
        User readUser = userDAO.getUserByID(newUser.getUserID());
        if (readUser != null) {
            System.out.println("✓ User found!");
            System.out.println("  Username: " + readUser.getUsername());
            System.out.println("  Role: " + readUser.getRole());
            System.out.println("  Email: " + readUser.getEmail());
        } else {
            System.out.println("✗ Failed to read user");
        }
        
        // READ by username
        System.out.println("\n[READ] Reading user by username...");
        User userByName = userDAO.getUserByUsername(newUser.getUsername());
        if (userByName != null) {
            System.out.println("✓ User found by username!");
            System.out.println("  UserID: " + userByName.getUserID());
        } else {
            System.out.println("✗ Failed to find user by username");
        }
        
        // READ all
        System.out.println("\n[READ] Reading all users...");
        List<User> allUsers = userDAO.getAllUsers();
        System.out.println("✓ Total users in database: " + allUsers.size());
        
        // UPDATE
        System.out.println("\n[UPDATE] Updating user email...");
        readUser.setEmail("updated@test.com");
        readUser.setPhone("9999999999");
        boolean updated = userDAO.updateUser(readUser);
        if (updated) {
            System.out.println("✓ User updated successfully!");
            
            // Verify update
            User verifyUser = userDAO.getUserByID(readUser.getUserID());
            System.out.println("  New email: " + verifyUser.getEmail());
            System.out.println("  New phone: " + verifyUser.getPhone());
        } else {
            System.out.println("✗ Failed to update user");
        }
        
        // DELETE (soft delete)
        System.out.println("\n[DELETE] Soft deleting user...");
        boolean deleted = userDAO.deleteUser(newUser.getUserID());
        if (deleted) {
            System.out.println("✓ User deleted (deactivated) successfully!");
            
            // Verify deletion
            User deletedUser = userDAO.getUserByID(newUser.getUserID());
            if (deletedUser != null) {
                System.out.println("  User still exists but IsActive = " + deletedUser.isActive());
            }
        } else {
            System.out.println("✗ Failed to delete user");
        }
        
        System.out.println("\n========================================\n");
    }
    
    // ========================================
    // TEST 2: PATIENT CRUD
    // ========================================
    private static void testPatientCRUD() {
        System.out.println("TEST 2: PATIENT CRUD OPERATIONS");
        System.out.println("========================================");
        
        PatientDAO patientDAO = new PatientDAO();
        UserDAO userDAO = new UserDAO();
        
        // First, create a user for the patient
        System.out.println("\n[SETUP] Creating user for patient...");
        User patientUser = new User();
        patientUser.setUsername("testpatient_" + System.currentTimeMillis());
        patientUser.setPasswordHash(PasswordUtil.hashPassword("test123"));
        patientUser.setRole("PATIENT");
        patientUser.setEmail("testpatient@test.com");
        patientUser.setPhone("5555555555");
        patientUser.setActive(true);
        
        userDAO.createUser(patientUser);
        System.out.println("✓ User created with ID: " + patientUser.getUserID());
        
        // CREATE Patient
        System.out.println("\n[CREATE] Creating patient record...");
        Patient newPatient = new Patient();
        newPatient.setUserID(patientUser.getUserID());
        newPatient.setFirstName("Test");
        newPatient.setLastName("Patient");
        newPatient.setDateOfBirth(LocalDate.of(1995, 3, 20));
        newPatient.setGender("Male");
        newPatient.setBloodGroup("A+");
        newPatient.setAddress("123 Test St, Boston, MA");
        newPatient.setEmergencyContact("1112223333");
        newPatient.setInsuranceID("TEST-INS-001");
        
        boolean created = patientDAO.createPatient(newPatient);
        if (created) {
            System.out.println("✓ Patient created successfully!");
            System.out.println("  PatientID: " + newPatient.getPatientID());
            System.out.println("  Name: " + newPatient.getFullName());
            System.out.println("  Age: " + newPatient.getAge() + " years");
        } else {
            System.out.println("✗ Failed to create patient");
            return;
        }
        
        // READ
        System.out.println("\n[READ] Reading patient by ID...");
        Patient readPatient = patientDAO.getPatientByID(newPatient.getPatientID());
        if (readPatient != null) {
            System.out.println("✓ Patient found!");
            System.out.println("  Name: " + readPatient.getFullName());
            System.out.println("  DOB: " + DateUtil.formatDate(readPatient.getDateOfBirth()));
            System.out.println("  Blood Group: " + readPatient.getBloodGroup());
        } else {
            System.out.println("✗ Failed to read patient");
        }
        
        // READ by UserID
        System.out.println("\n[READ] Reading patient by UserID...");
        Patient patientByUser = patientDAO.getPatientByUserID(patientUser.getUserID());
        if (patientByUser != null) {
            System.out.println("✓ Patient found by UserID!");
        } else {
            System.out.println("✗ Failed to find patient by UserID");
        }
        
        // READ all
        System.out.println("\n[READ] Reading all patients...");
        List<Patient> allPatients = patientDAO.getAllPatients();
        System.out.println("✓ Total patients in database: " + allPatients.size());
        
        // UPDATE
        System.out.println("\n[UPDATE] Updating patient information...");
        readPatient.setAddress("456 New Address, Boston, MA");
        readPatient.setEmergencyContact("9998887777");
        boolean updated = patientDAO.updatePatient(readPatient);
        if (updated) {
            System.out.println("✓ Patient updated successfully!");
            
            // Verify update
            Patient verifyPatient = patientDAO.getPatientByID(readPatient.getPatientID());
            System.out.println("  New address: " + verifyPatient.getAddress());
            System.out.println("  New emergency contact: " + verifyPatient.getEmergencyContact());
        } else {
            System.out.println("✗ Failed to update patient");
        }
        
        // DELETE
        System.out.println("\n[DELETE] Deleting patient...");
        boolean deleted = patientDAO.deletePatient(newPatient.getPatientID());
        if (deleted) {
            System.out.println("✓ Patient deleted successfully!");
            
            // Verify deletion
            Patient deletedPatient = patientDAO.getPatientByID(newPatient.getPatientID());
            if (deletedPatient == null) {
                System.out.println("  Patient record removed from database");
            }
        } else {
            System.out.println("✗ Failed to delete patient");
        }
        
        // Cleanup: Delete test user
        userDAO.deleteUser(patientUser.getUserID());
        
        System.out.println("\n========================================\n");
    }
    
    // ========================================
    // TEST 3: COMPLAINT CRUD
    // ========================================
    private static void testComplaintCRUD() {
        System.out.println("TEST 3: COMPLAINT CRUD OPERATIONS");
        System.out.println("========================================");
        
        ComplaintDAO complaintDAO = new ComplaintDAO();
        PatientDAO patientDAO = new PatientDAO();
        
        // Get first patient
        List<Patient> patients = patientDAO.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("✗ No patients found - cannot test complaints");
            return;
        }
        Patient testPatient = patients.get(0);
        
        // CREATE
        System.out.println("\n[CREATE] Creating complaint...");
        Complaint newComplaint = new Complaint();
        newComplaint.setPatientID(testPatient.getPatientID());
        newComplaint.setCategory("ROUTINE");
        newComplaint.setPriority("MEDIUM");
        newComplaint.setDescription("Persistent headache for the past 3 days. Pain is moderate and occurs mainly in the morning.");
        newComplaint.setStatus("SUBMITTED");
        
        boolean created = complaintDAO.createComplaint(newComplaint);
        if (created) {
            System.out.println("✓ Complaint created successfully!");
            System.out.println("  ComplaintID: " + newComplaint.getComplaintID());
            System.out.println("  Patient: " + testPatient.getFullName());
            System.out.println("  Category: " + newComplaint.getCategory());
            System.out.println("  Priority: " + newComplaint.getPriority());
        } else {
            System.out.println("✗ Failed to create complaint");
            return;
        }
        
        // READ
        System.out.println("\n[READ] Reading complaint by ID...");
        Complaint readComplaint = complaintDAO.getComplaintByID(newComplaint.getComplaintID());
        if (readComplaint != null) {
            System.out.println("✓ Complaint found!");
            System.out.println("  Description: " + readComplaint.getDescription());
            System.out.println("  Status: " + readComplaint.getStatus());
            System.out.println("  Created: " + DateUtil.formatDateTime(readComplaint.getCreatedDate()));
        } else {
            System.out.println("✗ Failed to read complaint");
        }
        
        // READ by patient
        System.out.println("\n[READ] Reading complaints by patient...");
        List<Complaint> patientComplaints = complaintDAO.getComplaintsByPatientID(testPatient.getPatientID());
        System.out.println("✓ Total complaints for patient: " + patientComplaints.size());
        
        // READ by status
        System.out.println("\n[READ] Reading complaints by status...");
        List<Complaint> submittedComplaints = complaintDAO.getComplaintsByStatus("SUBMITTED");
        System.out.println("✓ Total SUBMITTED complaints: " + submittedComplaints.size());
        
        // READ unassigned
        System.out.println("\n[READ] Reading unassigned complaints...");
        List<Complaint> unassigned = complaintDAO.getUnassignedComplaints();
        System.out.println("✓ Unassigned complaints: " + unassigned.size());
        
        // UPDATE - Assign to doctor
        System.out.println("\n[UPDATE] Assigning complaint to doctor...");
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> doctors = employeeDAO.getAllEmployees();
        if (!doctors.isEmpty()) {
            Employee doctor = doctors.get(0);
            boolean assigned = complaintDAO.assignComplaintToDoctor(
                newComplaint.getComplaintID(), 
                doctor.getEmployeeID()
            );
            
            if (assigned) {
                System.out.println("✓ Complaint assigned to Dr. " + doctor.getFullName());
                
                // Verify assignment
                Complaint assignedComplaint = complaintDAO.getComplaintByID(newComplaint.getComplaintID());
                System.out.println("  Status: " + assignedComplaint.getStatus());
                System.out.println("  AssignedDoctorID: " + assignedComplaint.getAssignedDoctorID());
            } else {
                System.out.println("✗ Failed to assign complaint");
            }
        }
        
        // UPDATE - Change status
        System.out.println("\n[UPDATE] Updating complaint status...");
        boolean statusUpdated = complaintDAO.updateComplaintStatus(
            newComplaint.getComplaintID(), 
            "IN_PROGRESS"
        );
        if (statusUpdated) {
            System.out.println("✓ Status updated to IN_PROGRESS");
            
            Complaint verifyComplaint = complaintDAO.getComplaintByID(newComplaint.getComplaintID());
            System.out.println("  New status: " + verifyComplaint.getStatus());
        } else {
            System.out.println("✗ Failed to update status");
        }
        
        // UPDATE - Full update
        System.out.println("\n[UPDATE] Updating complaint details...");
        readComplaint.setPriority("HIGH");
        readComplaint.setDescription(readComplaint.getDescription() + " [UPDATED]");
        boolean updated = complaintDAO.updateComplaint(readComplaint);
        if (updated) {
            System.out.println("✓ Complaint updated!");
            
            Complaint verifyComplaint = complaintDAO.getComplaintByID(readComplaint.getComplaintID());
            System.out.println("  New priority: " + verifyComplaint.getPriority());
        } else {
            System.out.println("✗ Failed to update complaint");
        }
        
        // DELETE
        System.out.println("\n[DELETE] Deleting complaint...");
        boolean deleted = complaintDAO.deleteComplaint(newComplaint.getComplaintID());
        if (deleted) {
            System.out.println("✓ Complaint deleted successfully!");
            
            // Verify deletion
            Complaint deletedComplaint = complaintDAO.getComplaintByID(newComplaint.getComplaintID());
            if (deletedComplaint == null) {
                System.out.println("  Complaint removed from database");
            }
        } else {
            System.out.println("✗ Failed to delete complaint");
        }
        
        System.out.println("\n========================================\n");
    }
    // ========================================
    // TEST 3: DRUG CRUD
    // ========================================
    private static void testDrugCRUD() {
        System.out.println("TEST 4: DRUG CRUD OPERATIONS");
        System.out.println("========================================");
        
        DrugDAO drugDAO = new DrugDAO();
        
        // CREATE
        System.out.println("\n[CREATE] Creating new drug...");
        Drug newDrug = new Drug();
        newDrug.setDrugName("Test Drug " + System.currentTimeMillis());
        newDrug.setGenericName("TestGeneric");
        newDrug.setCategory("Test Category");
        newDrug.setManufacturer("Test Pharma");
        newDrug.setDosageForm("TABLET");
        newDrug.setStrength("100mg");
        newDrug.setUnitPrice(10.50);
        newDrug.setStockQuantity(500);
        newDrug.setDescription("Test drug for CRUD testing");
        newDrug.setSideEffects("None - test drug");
        
        boolean created = drugDAO.createDrug(newDrug);
        if (created) {
            System.out.println("✓ Drug created successfully!");
            System.out.println("  DrugID: " + newDrug.getDrugID());
            System.out.println("  Name: " + newDrug.getDrugName());
            System.out.println("  Stock: " + newDrug.getStockQuantity());
        } else {
            System.out.println("✗ Failed to create drug");
            return;
        }
        
        // READ
        System.out.println("\n[READ] Reading drug by ID...");
        Drug readDrug = drugDAO.getDrugByID(newDrug.getDrugID());
        if (readDrug != null) {
            System.out.println("✓ Drug found!");
            System.out.println("  Name: " + readDrug.getDrugName());
            System.out.println("  Price: $" + readDrug.getUnitPrice());
            System.out.println("  Stock: " + readDrug.getStockQuantity());
        } else {
            System.out.println("✗ Failed to read drug");
        }
        
        // READ all
        System.out.println("\n[READ] Reading all drugs...");
        List<Drug> allDrugs = drugDAO.getAllDrugs();
        System.out.println("✓ Total drugs in catalog: " + allDrugs.size());
        
        // Search
        System.out.println("\n[READ] Searching drugs...");
        List<Drug> searchResults = drugDAO.searchDrugsByName("Para");
        System.out.println("✓ Search 'Para' found: " + searchResults.size() + " drugs");
        
        // Get by category
        System.out.println("\n[READ] Getting drugs by category...");
        List<Drug> analgesics = drugDAO.getDrugsByCategory("Analgesic");
        System.out.println("✓ Analgesic drugs: " + analgesics.size());
        
        // UPDATE
        System.out.println("\n[UPDATE] Updating drug price and stock...");
        readDrug.setUnitPrice(15.99);
        readDrug.setStockQuantity(750);
        boolean updated = drugDAO.updateDrug(readDrug);
        if (updated) {
            System.out.println("✓ Drug updated successfully!");
            
            Drug verifyDrug = drugDAO.getDrugByID(readDrug.getDrugID());
            System.out.println("  New price: $" + verifyDrug.getUnitPrice());
            System.out.println("  New stock: " + verifyDrug.getStockQuantity());
        } else {
            System.out.println("✗ Failed to update drug");
        }
        
        // UPDATE stock (add/remove)
        System.out.println("\n[UPDATE] Adjusting stock (remove 50 units)...");
        boolean stockUpdated = drugDAO.updateStock(readDrug.getDrugID(), -50);
        if (stockUpdated) {
            System.out.println("✓ Stock adjusted!");
            
            Drug verifyDrug = drugDAO.getDrugByID(readDrug.getDrugID());
            System.out.println("  Current stock: " + verifyDrug.getStockQuantity());
        } else {
            System.out.println("✗ Failed to update stock");
        }
        
        // Get low stock drugs
        System.out.println("\n[READ] Getting low stock drugs (threshold: 100)...");
        List<Drug> lowStock = drugDAO.getLowStockDrugs(100);
        System.out.println("✓ Drugs with stock ≤ 100: " + lowStock.size());
        
        // DELETE
        System.out.println("\n[DELETE] Deleting drug...");
        boolean deleted = drugDAO.deleteDrug(newDrug.getDrugID());
        if (deleted) {
            System.out.println("✓ Drug deleted successfully!");
        } else {
            System.out.println("✗ Failed to delete drug");
        }
        
        System.out.println("\n========================================\n");
    }
    
    // ========================================
    // TEST 4: COMPLETE WORKFLOW
    // ========================================
    private static void testCompleteWorkflow() {
        System.out.println("TEST 5: COMPLETE PATIENT WORKFLOW");
        System.out.println("========================================");
        System.out.println("Simulating: Patient → Complaint → Doctor → Assessment → Diagnosis → Treatment");
        System.out.println();
        
        AuthService authService = new AuthService();
        
        // Step 1: Patient logs in and submits complaint
        System.out.println("STEP 1: Patient submits complaint");
        User patient = authService.login("patient1", "password123");
        
        if (patient == null) {
            System.out.println("✗ Login failed");
            return;
        }
        
        ComplaintService complaintService = new ComplaintService();
        Complaint complaint = complaintService.submitComplaint(
            "EMERGENCY",
            "CRITICAL",
            "Severe abdominal pain, nausea, and vomiting for 6 hours"
        );
        
        if (complaint != null) {
            System.out.println("✓ Complaint C" + complaint.getComplaintID() + " submitted");
        } else {
            System.out.println("✗ Failed to submit complaint");
            authService.logout();
            return;
        }
        
        authService.logout();
        
        // Step 2: Doctor logs in and creates assessment
        System.out.println("\nSTEP 2: Doctor creates assessment");
        User doctor = authService.login("doctor1", "password123");
        
        if (doctor == null) {
            System.out.println("✗ Doctor login failed");
            return;
        }
        
        // First assign complaint to doctor
        ComplaintDAO complaintDAO = new ComplaintDAO();
        Employee currentDoctor = UserSession.getInstance().getCurrentEmployee();
        complaintDAO.assignComplaintToDoctor(complaint.getComplaintID(), currentDoctor.getEmployeeID());
        
        DoctorService doctorService = new DoctorService();
        Assessment assessment = doctorService.createAssessment(
            complaint.getComplaintID(),
            "Severe abdominal pain in lower right quadrant, nausea, fever",
            "Tenderness in RLQ, positive McBurney's sign",
            "Suspected appendicitis - recommend immediate CT scan"
        );
        
        if (assessment != null) {
            System.out.println("✓ Assessment A" + assessment.getAssessmentID() + " created");
        } else {
            System.out.println("✗ Failed to create assessment");
            authService.logout();
            return;
        }
        
        // Step 3: Doctor creates diagnosis
        System.out.println("\nSTEP 3: Doctor creates diagnosis");
        Diagnosis diagnosis = doctorService.createDiagnosis(
            assessment.getAssessmentID(),
            "K35.80",
            "Acute Appendicitis",
            "Acute appendicitis with localized peritonitis. Requires emergency appendectomy.",
            "SEVERE"
        );
        
        if (diagnosis != null) {
            System.out.println("✓ Diagnosis D" + diagnosis.getDiagnosisID() + " created");
            System.out.println("  Diagnosis: " + diagnosis.getDiagnosisName());
        } else {
            System.out.println("✗ Failed to create diagnosis");
            authService.logout();
            return;
        }
        
        // Step 4: Doctor creates treatment
        System.out.println("\nSTEP 4: Doctor creates treatment plan");
        Treatment treatment = doctorService.createTreatment(
            diagnosis.getDiagnosisID(),
            "Emergency appendectomy followed by IV antibiotics and pain management",
            "Amoxicillin 500mg, Paracetamol 500mg",
            "NPO (nothing by mouth), IV fluids, pre-op antibiotics",
            LocalDate.now().plusWeeks(2)
        );
        
        if (treatment != null) {
            System.out.println("✓ Treatment T" + treatment.getTreatmentID() + " created");
            System.out.println("  Follow-up: " + DateUtil.formatDate(treatment.getFollowUpDate()));
        } else {
            System.out.println("✗ Failed to create treatment");
            authService.logout();
            return;
        }
        
        authService.logout();
        
        // Step 5: Check notifications
        System.out.println("\nSTEP 5: Checking notifications generated");
        patient = authService.login("patient1", "password123");
        
        NotificationService notificationService = new NotificationService();
        int unreadCount = notificationService.getUnreadCount();
        System.out.println("✓ Patient has " + unreadCount + " unread notifications");
        
        List<Notification> notifications = notificationService.getUnreadNotifications();
        if (!notifications.isEmpty()) {
            System.out.println("\nRecent notifications:");
            for (int i = 0; i < Math.min(5, notifications.size()); i++) {
                System.out.println("  " + (i+1) + ". " + notifications.get(i).getMessage());
            }
        }
        
        authService.logout();
        
        System.out.println("\n✓ COMPLETE WORKFLOW TEST SUCCESSFUL!");
        System.out.println("  Complaint → Assessment → Diagnosis → Treatment → Notifications");
        System.out.println("\n========================================\n");
    }
}