package edu.pav.PatientTrackerSystem.commons;

import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private EmailService emailService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendSimpleMessage() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Content";

        emailService.sendSimpleMessage(to, subject, text);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }


    @Test
    public void testAlertUsers() {
        long doctorId = 1L;
        long patientId = 2L;

        ArrayList<Long> doctors = new ArrayList<>(Collections.singletonList(doctorId));
        ArrayList<Long> patients = new ArrayList<>(Collections.singletonList(patientId));

        HashMap<String, ArrayList<Long>> userList = new HashMap<>();
        userList.put("doctor", doctors);
        userList.put("patient", patients);

        String subject = "Alert Subject";
        String body = "Alert Body";

        Doctor doctor = Doctor.builder().email("doctor@example.com").doctorId(doctorId).build();

        Patient patient = Patient.builder().email("patient@example.com").patientId(patientId).build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        emailService.alertUsers(userList, subject, body);

        verify(emailSender, times(2)).send(any(SimpleMailMessage.class));
    }
}
