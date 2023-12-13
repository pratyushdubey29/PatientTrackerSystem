package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.LoginRequest;
import edu.pav.PatientTrackerSystem.commons.dto.LoginResponse;
import edu.pav.PatientTrackerSystem.commons.dto.PatientSignupRequest;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtTokenUtil;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtUserDetailsService;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for managing patient-related operations.
 */
@RestController
public class PatientController {

    @Autowired
    PasswordEncoder passwordEncoder;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

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
    public BaseResponse<PatientsLogin> signup(@RequestBody PatientSignupRequest request) {

        String appendedEmail = request.getAppendedEmail();
        assert appendedEmail.startsWith(Constants.PATIENT + ":");
        String userName = request.getAppendedEmail().substring(8);

        PatientsLogin signupDetails = PatientsLogin.builder()
                .username(userName)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(existsPatient(userName)) {
            return new BaseResponse<>(HttpStatus.CONFLICT, Constants.DOCTOR_ALREADY_PRESENT_STRING,
                    PatientsLogin.builder().build());
        }

        signupDetails = patientSignupRepository.save(signupDetails);

        Patient patient = Patient.builder()
                .patientId(signupDetails.getId())
                .email(userName)
                .name(request.getName())
                .address(request.getAddress())
                .dob(request.getDob())
                .sex(request.getSex())
                .height(request.getHeight())
                .weight(request.getWeight())
                .phoneNumber(request.getPhoneNumber())
                .build();

        patientRepository.save(patient);

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, PatientsLogin.builder().id(signupDetails.getId()).build());
    }

    @PostMapping(value = "/patients/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {

        String appendedEmail = request.getAppendedUsername();
        assert appendedEmail.startsWith(Constants.PATIENT + ":");
        String userName = request.getAppendedUsername().substring(8);

        authenticate(appendedEmail, request.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(appendedEmail);

        final String token = jwtTokenUtil.generateToken(userDetails, Constants.PATIENT);


        Patient patient= patientRepository.findByEmail(userName);
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, LoginResponse.builder().userId(patient.getPatientId()).token(token).build());
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
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
        return patientSignupRepository.findByUsername(email) != null;
    }

}
