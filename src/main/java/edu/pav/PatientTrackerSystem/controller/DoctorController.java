package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorSignupRequest;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorProfileEditRequest;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;

import javax.print.Doc;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for handling operations related to doctors, including retrieval, filtering,
 * and signup functionalities.
 */
@RestController
public class DoctorController {

    /**
     * Repository for accessing doctor data.
     */
    @Autowired
    DoctorRepository doctorRepository;

    /**
     * Repository for accessing doctor signup details.
     */
    @Autowired
    DoctorSignupRepository doctorSignupRepository;

    /**
     * Retrieves a list of all approved doctors.
     *
     * @return BaseResponse containing the list of approved doctors.
     */
    @GetMapping(value = "/doctors")
    public BaseResponse<List<Doctor>> getAllApprovedDoctors() {
        List<Doctor> responseData = filterDoctorByStatus(doctorRepository.findAll(), true);
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, responseData);
    }

    /**
     * Retrieves a list of all unapproved doctors.
     *
     * @return BaseResponse containing the list of unapproved doctors.
     */
    @GetMapping(value = "/unapproved-doctors")
    public BaseResponse<List<Doctor>> getAllUnapprovedDoctors() {
        List<Doctor> responseData = filterDoctorByStatus(doctorRepository.findAll(), false);
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, responseData);
    }

    /**
     * Retrieves a specific doctor by ID.
     *
     * @param id The ID of the doctor to retrieve.
     * @return BaseResponse containing the retrieved doctor.
     */
    @GetMapping(value = "/doctors/{id}")
    public BaseResponse<Doctor> getDoctorById(@PathVariable("id") Long id) {
        Optional<Doctor> retrievedDoctor = doctorRepository.findById(id);
        return retrievedDoctor.map(doctor -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, doctor))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.DOCTOR_ID_NOT_FOUND_STRING, Doctor.builder().build()));
    }

    /**
     * Searches for doctors based on speciality, name, and address.
     *
     * @param speciality The speciality to filter by.
     * @param name       The name to filter by.
     * @param address    The address to filter by.
     * @return BaseResponse containing the list of matching doctors.
     */
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

    /**
     * Deletes a doctor by their unique identifier.
     *
     * @param id The unique identifier of the doctor to be deleted.
     */
//    @DeleteMapping(value = "/doctors/{id}")
    private void deleteDoctor(@PathVariable("id") Long id) {
        if (id != null) {
            doctorRepository.deleteById(id);
        }
    }

    /**
     * Filters a list of doctors based on their approval status.
     *
     * @param inputDoctors   The list of doctors to be filtered.
     * @param approvalStatus The approval status used for filtering.
     * @return A filtered list of doctors.
     */
    private List<Doctor> filterDoctorByStatus(List<Doctor> inputDoctors, boolean approvalStatus) {
        return inputDoctors.stream()
                .filter(doctor -> doctor.getIsApproved() == approvalStatus)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a doctor with the given email exists in the system.
     *
     * @param email The email of the doctor to check for existence.
     * @return {@code true} if a doctor with the given email exists, {@code false} otherwise.
     */
    private Boolean existsDoctor(String email) {
        return doctorRepository.findByEmail(email) != null;
    }

    /**
     * Signs up a new doctor.
     *
     * @param request The DoctorSignupRequest containing signup details.
     * @return BaseResponse indicating the success of the signup process.
     */
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
                .loginKey(UserLoginKey.builder()
                        .userId(doctor.getDoctorId())
                        .userName(doctor.getEmail())
                        .build())
                .password(Utils.encryptPassword(request.getPassword()))
                .build();

         DoctorsLogin doctorsLogin = doctorSignupRepository.save(signupDetails);

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }

    /**
     * Edits the details of a doctor.
     *
     * @param request The DoctorProfileEditRequest containing edit details.
     * @return BaseResponse indicating the success of the edit process.
     */
    @PostMapping(value = "/doctors/edit")
    public BaseResponse editProfile(@RequestBody DoctorProfileEditRequest request) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(request.getDoctorId());

        if (optionalDoctor.isPresent()) {
            Doctor existingDoctor = optionalDoctor.get();

            existingDoctor.setAddress(request.getAddress());
            existingDoctor.setHospital(request.getHospital());
            existingDoctor.setSpeciality(request.getSpeciality());
            existingDoctor.setPhoneNumber(request.getPhoneNumber());

            Doctor updatedDoctor = doctorRepository.save(existingDoctor);
            return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, updatedDoctor);
        } else {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, Constants.DOCTOR_ID_NOT_FOUND_STRING, Doctor.builder().build());
        }
    }

}
