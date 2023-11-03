package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/appointments/{id}")
    public Optional<Appointment> getAppointmentById(@PathVariable("id") Long id) {
        return appointmentRepository.findById(id);
    }

}
