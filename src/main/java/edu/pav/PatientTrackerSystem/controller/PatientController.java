package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
//import edu.pav.PatientTrackerSystem.commons.auth.patient.MyUserDetailsService;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.LoginDto;
import edu.pav.PatientTrackerSystem.commons.dto.PatientSignupRequest;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
//import edu.pav.PatientTrackerSystem.repository.PatientRoleRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PatientController {

    @Autowired
    PatientRepository patientRepository;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private PatientRepository patientRepository;
//
////    @Autowired
////    private PatientRoleRepository roleRepository;
//
//    @Autowired
//    private MyUserDetailsService userDetailsService;
//
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    PatientSignupRepository patientSignupRepository;

    @GetMapping("/patients")
    public BaseResponse getAllPatients(){
        return  new BaseResponse(HttpStatus.OK, Constants.SUCCESS, patientRepository.findAll());
    }

    @GetMapping("/patients/{id}")
    public BaseResponse getPatientById(@PathVariable("id") Long id){
        Optional<Patient> retrievedPatient = patientRepository.findById(id);
        return retrievedPatient.map(patient -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, patient))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.PATIENT_ID_NOT_FOUND_STRING, Patient.builder().build()));
    }

//    @PostMapping(value = "/patients/login")
//    public BaseResponse login(@RequestBody LoginDto loginDto) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new BaseResponse(HttpStatus.OK, "User login successfully!...", null);
//    }

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
                .userName(patient.getEmail())
                .userId(patient.getPatientId())
                .password(Utils.encryptPassword(request.getPassword()))
                .build();

        patientSignupRepository.save(signupDetails);

        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }

    //    @DeleteMapping(value = "/patient/{id}")
    private void deletePatient(@PathVariable("id") Long id) {
        if (id != null) {
            patientRepository.deleteById(id);
        }
    }

    private Boolean existsPatient(String email) {
        return patientRepository.findByEmail(email) != null;
    }

}
