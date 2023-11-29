package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorSignupRequest;
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
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, responseData);
    }

    @GetMapping(value = "/unapproved-doctors")
    public BaseResponse<List<Doctor>> getAllUnapprovedDoctors() {
        List<Doctor> responseData = filterDoctorByStatus(doctorRepository.findAll(), false);
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, responseData);
    }

    @GetMapping(value = "/doctors/{id}")
    public BaseResponse<Doctor> getDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> retrievedDoctor = doctorRepository.findById(id);
        return retrievedDoctor.map(doctor -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, doctor))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.DOCTOR_ID_NOT_FOUND_STRING, Doctor.builder().build()));
    }

    @GetMapping(value = "/doctors/findMatchByAll")
    public BaseResponse<List<Doctor>> findDoctorMatchByAll(@RequestParam(required = false) String speciality,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) String address) {
        speciality = (speciality != null) ? speciality : Constants.EMPTY_STRING;
        name = (name != null) ? name : Constants.EMPTY_STRING;
        address = (address != null) ? address : Constants.EMPTY_STRING;

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS,  filterDoctorByStatus(
                doctorRepository.find(speciality, name, address), true));
    }

//    @DeleteMapping(value = "/doctors/{id}")
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
    public BaseResponse signup(@RequestBody DoctorSignupRequest request) {

        if(existsDoctor(request.getEmail())) {
            return new BaseResponse(HttpStatus.CONFLICT, Constants.DOCTOR_ALREADY_PRESENT_STRING,
                    DoctorsLogin.builder().build());
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
                .userName(doctor.getEmail())
                .userId(doctor.getDoctorId())
                .password(Utils.encryptPassword(request.getPassword()))
                .build();

         doctorSignupRepository.save(signupDetails);

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }
}
