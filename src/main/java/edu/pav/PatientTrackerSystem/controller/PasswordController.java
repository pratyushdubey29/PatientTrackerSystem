package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.EmailService;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.OTPRequest;
import edu.pav.PatientTrackerSystem.commons.dto.ResetRequest;
import edu.pav.PatientTrackerSystem.model.*;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
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


    private final Map<Pair<String,String>,  String> otpStorage = new HashMap<>();

    /**
     * Generates and sends an OTP to the provided email address based on the user type.
     *
     * @param otpRequest An object containing user type and email to send the OTP.
     * @return BaseResponse indicating the status of the OTP generation and sending process.
     */
    @PostMapping(value = "/reset-password/get-otp")
    public BaseResponse getOTP(@RequestBody OTPRequest otpRequest) {
        String generatedOTP = generateOTP();
        Pair<String, String> key = Pair.of(otpRequest.getUserType(), otpRequest.getEmail());
        otpStorage.put(key, generatedOTP);
        emailService.sendSimpleMessage(otpRequest.getEmail(), Constants.OTP_EMAIL_SUBJECT_STRING, generatedOTP);
        return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
    }

    /**
     * Resets the password for the user associated with the provided email and OTP.
     *
     * @param resetRequest An object containing user type, email, OTP, and new password.
     * @return BaseResponse indicating the status of the password reset process.
     */
    @PostMapping(value = "/reset-password/reset")
    public BaseResponse resetPassword(@RequestBody ResetRequest resetRequest){
        String email = resetRequest.getEmail();
        String receivedOTP = resetRequest.getOTP();
        String userType = resetRequest.getUserType();
        String storedOTP = otpStorage.get(Pair.of(userType, email));

        if (storedOTP != null && storedOTP.equals(receivedOTP)) {
            if (userType.equals(Constants.DOCTOR)){
                Doctor d = doctorRepository.findByEmail(email);
                DoctorsLogin signupDetails = DoctorsLogin.builder()
                        .loginKey(UserLoginKey.builder()
                                .userId(d.getDoctorId())
                                .userName(email)
                                .build())
                        .password(Utils.encryptPassword(resetRequest.getPassword()))
                        .build();

                DoctorsLogin doctorsLogin = doctorSignupRepository.save(signupDetails);
            } else if (userType.equals(Constants.PATIENT)) {
                Patient p = patientRepository.findByEmail(email);
                PatientsLogin signupDetails = PatientsLogin.builder()
                        .loginKey(UserLoginKey.builder().userId(p.getPatientId()).userName(email)
                                .build())
                        .password(Utils.encryptPassword(resetRequest.getPassword()))
                        .build();

                patientSignupRepository.save(signupDetails);
            }
            emailService.sendSimpleMessage(email, Constants.PASSWORD_RESET, Constants.PASSWORD_RESET);
            otpStorage.remove(Pair.of(userType, email));
            return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, Constants.SUCCESS);
        } else {
            return new BaseResponse(HttpStatus.OK, Constants.INVALID_OTP_STRING, Constants.INVALID_OTP_STRING);
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
}
