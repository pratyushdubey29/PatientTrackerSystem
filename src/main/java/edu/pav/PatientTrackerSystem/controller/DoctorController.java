package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping("/doctors")
    public List<Doctor> getAllApprovedDoctors() {
        List<Doctor> responseList = doctorRepository.findAll();
        return filterDoctorByStatus(responseList, true);
    }

    @GetMapping("/unapproved-doctors")
    public List<Doctor> getAllUnapprovedDoctors() {
        List<Doctor> responseList = doctorRepository.findAll();
        return filterDoctorByStatus(responseList, false);
    }

    @GetMapping("/doctors/{id}")
    public List<Doctor> getDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> retrievedDoctor = doctorRepository.findById(id);
        if (retrievedDoctor.isPresent() && retrievedDoctor.get().getIsApproved()) {
            return Collections.singletonList(retrievedDoctor.get());
        } else {
            return Collections.emptyList();
        }
    }

    @RequestMapping(path = "/doctors/findMatchByAll", method = RequestMethod.GET)
    public List<Doctor> findDoctorMatchByAll(@RequestParam(required = false) String speciality,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String address) {
        speciality = (speciality != null) ? speciality : "";
        name = (name != null) ? name : "";
        address = (address != null) ? address : "";

//        if (speciality.isEmpty() && name.isEmpty() && address.isEmpty()) {
//            return Collections.emptyList();
//        }
        List<Doctor> responseList = doctorRepository.find(speciality, name, address);
        return filterDoctorByStatus(responseList, true);
    }

    @RequestMapping(path = "/doctors/findMatchBySome", method = RequestMethod.GET)
    public List<Doctor> findDoctorMatchBySome(@RequestParam(required = false) String speciality,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) String address) {
        List<Doctor> responseList = doctorRepository.findBySpecialityContainingOrNameContainingOrAddressContaining(
                speciality, name, address);
        return filterDoctorByStatus(responseList, true);
    }

    private List<Doctor> filterDoctorByStatus(List<Doctor> inputDoctors, boolean approvalStatus) {
        if (approvalStatus) {
            return inputDoctors.stream().filter(Doctor::getIsApproved).collect(Collectors.toList());
        } else {
            return inputDoctors.stream().filter(doctor -> !doctor.getIsApproved()).collect(Collectors.toList());
        }

    }
}
