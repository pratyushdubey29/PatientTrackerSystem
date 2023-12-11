package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.PatientProfileEditRequest;
import edu.pav.PatientTrackerSystem.commons.dto.PatientSignupRequest;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for managing patient-related operations.
 */
@RestController
public class PatientController {

    /**
     * Repository for accessing patient data.
     */
    @Autowired
    PatientRepository patientRepository;

    /**
     * Repository for accessing patient signup details.
     */
    @Autowired
    PatientSignupRepository patientSignupRepository;

    /**
     * Retrieves all patients.
     *
     * @return BaseResponse containing the list of patients.
     */
    @GetMapping("/patients")
    public BaseResponse getAllPatients(){
        return  new BaseResponse(HttpStatus.OK, Constants.SUCCESS, patientRepository.findAll());
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id The ID of the patient.
     * @return BaseResponse containing the patient information.
     */
    @GetMapping("/patients/{id}")
    public BaseResponse getPatientById(@PathVariable("id") Long id){
        Optional<Patient> retrievedPatient = patientRepository.findById(id);
        return retrievedPatient.map(patient -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, patient))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.PATIENT_ID_NOT_FOUND_STRING, Patient.builder().build()));
    }

    /**
     * Registers a new patient.
     *
     * @param request The PatientSignupRequest containing patient information.
     * @return BaseResponse indicating the success or failure of the registration.
     */
    @Transactional
    @PostMapping(value = "/patients/signup")
    public BaseResponse signup(@RequestBody PatientSignupRequest request) {

        if (existsPatient(request.getEmail())) {
            return new BaseResponse(HttpStatus.CONFLICT, Constants.PATIENT_ALREADY_PRESENT_STRING,
                    PatientsLogin.builder().build());
        }

        Patient patient = Patient.builder()
                .name(request.getName()).email(request.getEmail())
                .address(request.getAddress()).dob(request.getDob())
                .sex(request.getSex()).height(request.getHeight())
                .weight(request.getWeight()).phoneNumber(request.getPhoneNumber())
                .build();

        patient = patientRepository.save(patient);

        PatientsLogin signupDetails = PatientsLogin.builder()
                .loginKey(UserLoginKey.builder().userId(patient.getPatientId()).userName(patient.getEmail())
                        .build())
                .password(Utils.encryptPassword(request.getPassword()))
                .build();

        patientSignupRepository.save(signupDetails);

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }

    /**
     * Deletes a patient with the specified ID.
     *
     * @param id The ID of the patient to be deleted.
     */
    //    @DeleteMapping(value = "/patient/{id}")
    private void deletePatient(@PathVariable("id") Long id) {
        if (id != null) {
            patientRepository.deleteById(id);
        }
    }

    /**
     * Checks if a patient with the specified email exists.
     *
     * @param email The email to check for the existence of a patient.
     * @return {@code true} if a patient with the given email exists, {@code false} otherwise.
     */
    private Boolean existsPatient(String email) {
        return patientRepository.findByEmail(email) != null;
    }

    /**
     * Edits the details of a patient.
     *
     * @param request The PatientProfileEditRequest containing edit details.
     * @return BaseResponse indicating the success of the edit process.
     */
    @PostMapping(value = "/patients/edit")
    public BaseResponse editProfile(@RequestBody PatientProfileEditRequest request) {
        Optional<Patient> optionalPatient = patientRepository.findById(request.getPatientId());

        if (optionalPatient.isPresent()) {
            Patient existingPatient = optionalPatient.get();

            existingPatient.setAddress(request.getAddress());
            existingPatient.setHeight(request.getHeight());
            existingPatient.setWeight(request.getWeight());
            existingPatient.setPhoneNumber(request.getPhoneNumber());

            Patient updatePatient = patientRepository.save(existingPatient);
            return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, updatePatient);
        } else {
            return new BaseResponse<>(HttpStatus.NOT_FOUND, Constants.PATIENT_ID_NOT_FOUND_STRING, Patient.builder().build());
        }
    }
}
