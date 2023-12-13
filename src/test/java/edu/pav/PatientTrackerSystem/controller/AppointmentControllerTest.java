package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.EmailService;
import edu.pav.PatientTrackerSystem.commons.EmailServiceTest;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import edu.pav.PatientTrackerSystem.repository.CaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AppointmentControllerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailService emailService;
//
//    @Mock
//    private JavaMailSender emailSender;

    @InjectMocks
    private AppointmentController appointmentController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllAppointments() {
        Appointment appointment = Appointment.builder().appointmentId(1L).build();
        when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(appointment));

        BaseResponse<List<Appointment>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(Collections.singletonList(appointment), response.getBody());
    }

    @Test
    public void testBookAppointment() {
        Appointment appointment = Appointment.builder()
                .appointmentId(1L)
                .caseId(1L)
                .patientId(1L)
                .doctorId(1L)
                .date("2023-12-25")
                .time("10:00 AM")
                .build();

        when(appointmentRepository.findByDoctorIdAndDateAndTime(1L, "2023-12-25", "10:00 AM"))
                .thenReturn(Collections.emptyList());// Doctor is free
        when(appointmentRepository.findByPatientIdAndDateAndTime(1L, "2023-12-25", "10:00 AM"))
                .thenReturn(Collections.emptyList());// Patient is free
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        BaseResponse response = appointmentController.bookAppointment(appointment, "Subject", "Body");

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SCHEDULE_APPOINTMENT_SUCCESSFUL, response.getMsg());
        assertEquals(appointment, response.getBody());

        // When Doctor is booked
        when(appointmentRepository.findByDoctorIdAndDateAndTime(1L, "2023-12-25", "10:00 AM"))
                .thenReturn(Collections.singletonList(appointment)); // Doctor is already booked
        when(appointmentRepository.findByPatientIdAndDateAndTime(1L, "2023-12-25", "10:00 AM"))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        BaseResponse secondResponse = appointmentController.bookAppointment(appointment, "Subject", "Body");

        assertEquals(HttpStatus.OK, secondResponse.getStatus());
        assertEquals(Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, secondResponse.getMsg());
        assertEquals(Appointment.builder().build(), secondResponse.getBody());
    }



}
