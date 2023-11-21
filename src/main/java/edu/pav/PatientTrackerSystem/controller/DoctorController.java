package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping(value = "/doctors")
    public BaseResponse<List<Doctor>> getAllApprovedDoctors() {
        List<Doctor> responseData = filterDoctorByStatus(doctorRepository.findAll(), true);
        return new BaseResponse<>(HttpStatus.OK, "Success!", responseData);
    }

    @GetMapping(value = "/unapproved-doctors")
    public BaseResponse<List<Doctor>> getAllUnapprovedDoctors() {
        List<Doctor> responseData = filterDoctorByStatus(doctorRepository.findAll(), false);
        return new BaseResponse<>(HttpStatus.OK, "Success!", responseData);
    }

    @GetMapping(value = "/doctors/{id}")
    public BaseResponse<Doctor> getDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> retrievedDoctor = doctorRepository.findById(id);
        if (retrievedDoctor.isPresent() && retrievedDoctor.get().getIsApproved()) {
            return new BaseResponse<>(HttpStatus.OK, "Success!", retrievedDoctor.get());
        } else {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, "Doctor ID not found!", Doctor.builder().build());
        }
    }

    @GetMapping(value = "/doctors/findMatchByAll")
    public BaseResponse<List<Doctor>> findDoctorMatchByAll(@RequestParam(required = false) String speciality,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String address) {
        speciality = (speciality != null) ? speciality : "";
        name = (name != null) ? name : "";
        address = (address != null) ? address : "";

        return new BaseResponse<>(HttpStatus.OK, "Success!",  filterDoctorByStatus(
                doctorRepository.find(speciality, name, address), true));
    }

    @DeleteMapping(value = "/doctors/{id}")
    private void deleteDoctor(@PathVariable("id") Long id) {
        if (id != null) {
            doctorRepository.deleteById(id);
        }
    }

    private List<Doctor> filterDoctorByStatus(List<Doctor> inputDoctors, boolean approvalStatus) {
        return inputDoctors.stream()
                .filter(doctor -> doctor.getIsApproved() == approvalStatus)
                .collect(Collectors.toList());
    }
}
