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

@AllArgsConstructor
@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("patienttrackingsystem.admn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public String getPatientEmail(long patientId){
        Optional<Patient> patient = patientRepository.findById(patientId);
        String patientEmail = "";
        if (patient.isPresent()){
            patientEmail = patient.get().getEmail();
        }
        return patientEmail;
    }

    public String getDoctorEmail(long doctorId){
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        String doctorEmail = "";
        if (doctor.isPresent()){
            doctorEmail = doctor.get().getEmail();
        }
        return doctorEmail;
    }

    public void alertUsers(HashMap<String, ArrayList<Long>> userList, String subject, String body){
        for (Map.Entry<String, ArrayList<Long>> entry : userList.entrySet()) {
            String key = entry.getKey();
            ArrayList<Long> values = entry.getValue();
            for (Long value : values) {
                String email = "";
                if (Objects.equals(key, Constants.DOCTOR)){
                    email = getDoctorEmail(value);
                }
                else if (Objects.equals(key, Constants.PATIENT)){
                    email = getPatientEmail(value);
                }
                sendSimpleMessage(email, subject, body);
            }
        }
    }

}
