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
import util.PasswordUtil;
import java.time.LocalDate;
import model.User;

public class RegistrationService {

    private UserDAO userDAO;
    private PatientDAO patientDAO;

    public RegistrationService() {
        this.userDAO = new UserDAO();
        this.patientDAO = new PatientDAO();
    }

    // Register new patient
    public boolean registerPatient(String username, String password, String email, String phone,
            String firstName, String lastName, LocalDate dob,
            String gender, String bloodGroup) {

        // Check if username exists
        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            System.err.println("Username already exists");
            return false;
        }

        // Create user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtil.hashPassword(password));
        newUser.setRole("PATIENT");
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setActive(true);

        boolean userCreated = userDAO.createUser(newUser);

        if (!userCreated) {
            System.err.println("Failed to create user");
            return false;
        }

        // Create patient
        Patient newPatient = new Patient();
        newPatient.setUserID(newUser.getUserID());
        newPatient.setFirstName(firstName);
        newPatient.setLastName(lastName);
        newPatient.setDateOfBirth(dob);
        newPatient.setGender(gender);
        newPatient.setBloodGroup(bloodGroup);
        newPatient.setEmergencyContact(phone);
        newPatient.setInsuranceID("");

        boolean patientCreated = patientDAO.createPatient(newPatient);

        if (patientCreated) {
            System.out.println("Patient registered: " + username);
            return true;
        } else {
            System.err.println("Failed to create patient");
            userDAO.deleteUser(newUser.getUserID());
            return false;
        }
    }

    // Check username availability
    public boolean isUsernameAvailable(String username) {
        User user = userDAO.getUserByUsername(username);
        return user == null;
    }
}
