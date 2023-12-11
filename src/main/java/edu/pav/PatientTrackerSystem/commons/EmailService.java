package edu.pav.PatientTrackerSystem.commons;

import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Service handling email-related functionalities like sending emails to doctors and patients.
 */
@AllArgsConstructor
@Component
public class EmailService {
    /**
     * JavaMailSender instance to send emails.
     */
    @Autowired
    private JavaMailSender emailSender;
    /**
     * Repository for accessing doctor-related data.
     */
    @Autowired
    private DoctorRepository doctorRepository;
    /**
     * Repository for accessing patient-related data.
     */
    @Autowired
    private PatientRepository patientRepository;

    /**
     * Sends a simple email message.
     *
     * @param to      Email address of the recipient.
     * @param subject Subject of the email.
     * @param text    Content of the email.
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("patienttrackingsystem.admn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Retrieves the email of the patient based on the provided patient ID.
     *
     * @param patientId ID of the patient.
     * @return The email address of the patient.
     */
    public String getPatientEmail(long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        String patientEmail = "";
        if (patient.isPresent()) {
            patientEmail = patient.get().getEmail();
        }
        return patientEmail;
    }

    /**
     * Retrieves the email of the doctor based on the provided doctor ID.
     *
     * @param doctorId ID of the doctor.
     * @return The email address of the doctor.
     */
    public String getDoctorEmail(long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        String doctorEmail = "";
        if (doctor.isPresent()) {
            doctorEmail = doctor.get().getEmail();
        }
        return doctorEmail;
    }

    /**
     * Alerts users (doctors and patients) by sending emails based on a provided list of IDs.
     *
     * @param userList A HashMap containing lists of user IDs categorized by user type (e.g., doctor or patient).
     * @param subject  Subject of the alert email.
     * @param body     Body content of the alert email.
     */
    public void alertUsers(HashMap<String, ArrayList<Long>> userList, String subject, String body) {
        for (Map.Entry<String, ArrayList<Long>> entry : userList.entrySet()) {
            String key = entry.getKey();
            ArrayList<Long> values = entry.getValue();
            for (Long value : values) {
                String email = "";
                if (Objects.equals(key, Constants.DOCTOR)) {
                    email = getDoctorEmail(value);
                } else if (Objects.equals(key, Constants.PATIENT)) {
                    email = getPatientEmail(value);
                }
                sendSimpleMessage(email, subject, body);
            }
        }
    }

}
