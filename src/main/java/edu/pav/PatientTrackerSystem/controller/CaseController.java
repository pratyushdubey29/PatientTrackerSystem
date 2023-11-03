package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.model.Case;
import edu.pav.PatientTrackerSystem.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CaseController {

    @Autowired
    CaseRepository caseRepository;

    @GetMapping("/cases")
    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }

    @GetMapping("/cases/{id}")
    public Optional<Case> getCaseById(@PathVariable("id") Long id) {
        return caseRepository.findById(id);
    }

}
