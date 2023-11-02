package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/doctors/{id}")
    public Optional<Doctor> getDoctorById(@PathVariable("id") Long id) {
        return doctorRepository.findById(id);
    }

}
