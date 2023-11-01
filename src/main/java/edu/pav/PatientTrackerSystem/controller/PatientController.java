package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @GetMapping("/patients")
    public List<Patient> getAllPatients(){
        return  patientRepository.findAll();
    }

    @GetMapping("/patients/{id}")
    public Optional<Patient> getPatientById(@PathVariable("id") Long id){
        return patientRepository.findById(id);
    }
}
