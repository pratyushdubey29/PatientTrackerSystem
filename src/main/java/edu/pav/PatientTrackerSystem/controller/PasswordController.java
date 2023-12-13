package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.EmailService;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.LoginResponse;
import edu.pav.PatientTrackerSystem.commons.dto.OTPRequest;
import edu.pav.PatientTrackerSystem.commons.dto.ResetRequest;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtTokenUtil;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtUserDetailsService;
import edu.pav.PatientTrackerSystem.model.*;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Controller class for managing updates in passwords.
 */
@RestController
public class PasswordController {

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
     * Repository for accessing doctor data.
     */
    @Autowired
    DoctorRepository doctorRepository;

    /**
     * Repository for accessing doctor signup details.
     */
    @Autowired
    DoctorSignupRepository doctorSignupRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;


    private final Map<String,  String> otpStorage = new HashMap<>();

    /**
     * Generates and sends an OTP to the provided email address based on the user type.
     *
     * @param otpRequest An object containing user type and email to send the OTP.
     * @return BaseResponse indicating the status of the OTP generation and sending process.
     */
    @PostMapping(value = "/reset-password/get-otp")
    public BaseResponse getOTP(@RequestBody OTPRequest otpRequest) {
        String email;
        if (otpRequest.getAppendedEmail().startsWith(Constants.DOCTOR)) {
            email = otpRequest.getAppendedEmail().substring(7);
        } else {
            email = otpRequest.getAppendedEmail().substring(8);
        }
        String generatedOTP = generateOTP();
        otpStorage.put(otpRequest.getAppendedEmail(), generatedOTP);
        emailService.sendSimpleMessage(email, Constants.OTP_EMAIL_SUBJECT_STRING, generatedOTP);
        return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }

    /**
     * Resets the password for the user associated with the provided email and OTP.
     *
     * @param resetRequest An object containing user type, email, OTP, and new password.
     * @return BaseResponse indicating the status of the password reset process.
     */
    @PostMapping(value = "/reset-password/reset")
    public BaseResponse<LoginResponse> resetPassword(@RequestBody ResetRequest resetRequest) throws Exception {
        String email;
        String userType;
        if (resetRequest.getAppendedEmail().startsWith(Constants.DOCTOR)) {
            email = resetRequest.getAppendedEmail().substring(7);
            userType = Constants.DOCTOR;
        } else {
            email = resetRequest.getAppendedEmail().substring(8);
            userType = Constants.PATIENT;
        }

        String receivedOTP = resetRequest.getOTP();
        String storedOTP = otpStorage.get(resetRequest.getAppendedEmail());

        Long userId;
        String token;

        if (storedOTP != null && storedOTP.equals(receivedOTP)) {
            if (userType.equals(Constants.DOCTOR)){
                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(resetRequest.getAppendedEmail());

                token = jwtTokenUtil.generateToken(userDetails, Constants.DOCTOR);

                DoctorsLogin existing = doctorSignupRepository.findByUsername(email);

                existing.setPassword(passwordEncoder.encode(resetRequest.getPassword()));

                existing = doctorSignupRepository.save(existing);
                userId = existing.getId();
            } else {

                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(resetRequest.getAppendedEmail());

                token = jwtTokenUtil.generateToken(userDetails, Constants.PATIENT);

                PatientsLogin existing = patientSignupRepository.findByUsername(email);

                existing.setPassword(passwordEncoder.encode(resetRequest.getPassword()));

                existing = patientSignupRepository.save(existing);
                userId = existing.getId();
            }
            emailService.sendSimpleMessage(email, Constants.PASSWORD_RESET, Constants.PASSWORD_RESET);
            otpStorage.remove(resetRequest.getAppendedEmail());
            return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, LoginResponse.builder().userId(userId).token(token).build());
        } else {
            return new BaseResponse<>(HttpStatus.OK, Constants.INVALID_OTP_STRING, LoginResponse.builder().build());
        }
    }

    /**
     * Generates a random six-digit OTP.
     *
     * @return A string containing the generated OTP.
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
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
}
