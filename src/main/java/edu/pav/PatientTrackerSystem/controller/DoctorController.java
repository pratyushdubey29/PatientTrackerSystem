package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorSignupRequest;
import edu.pav.PatientTrackerSystem.commons.utils;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    DoctorSignupRepository doctorSignupRepository;


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

    private Boolean existsDoctor(String email) {
        return doctorRepository.findByEmail(email) != null;
    }

    @Transactional
    @PostMapping(value = "/doctors/signup")
    public BaseResponse<String> signup(@RequestBody DoctorSignupRequest request) {

        if(existsDoctor(request.getEmail())) {
            return BaseResponse.<String>builder()
                    .status(HttpStatus.CONFLICT)
                    .msg("Doctor with this email already exists")
                    .body("Failed persisting")
                    .build();
        }

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .hospital(request.getHospital())
                .dob(request.getDob())
                .speciality(request.getSpeciality())
                .phoneNumber(request.getPhoneNumber())
                .isApproved(Boolean.FALSE)
                .build();

        doctor = doctorRepository.save(doctor);

        DoctorsLogin signupDetails = DoctorsLogin.builder()
                .loginKey(UserLoginKey.builder()
                        .userId(doctor.getDoctorId())
                        .userName(doctor.getEmail())
                        .build())
                .password(utils.encryptPassword(request.getPassword()))
                .build();

        doctorSignupRepository.save(signupDetails);

        return new BaseResponse<>(HttpStatus.OK, "Success!", "persistence was successful!");
    }
}
