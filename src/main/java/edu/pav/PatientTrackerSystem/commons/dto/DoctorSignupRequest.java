package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * DoctorSignupRequest class represents the data structure for doctor signup requests.
 * It includes essential information such as email, password, date of birth, name, hospital,
 * speciality, address, and phone number.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class DoctorSignupRequest {

    /**
     * The email address of the doctor.
     */
    @NonNull
    private String appendedEmail;
    
    /**
     * The password associated with the doctor's account.
     */
    @NonNull
    private String password;
    
    /**
     * The date of birth of the doctor in the format "YYYY-MM-DD".
     */
    @NonNull
    private String dob;
    
    /**
     * The name of the doctor.
     */
    @NonNull
    private String name;

    /**
     * The hospital where the doctor is affiliated.
     */
    @NonNull
    private String hospital;

    /**
     * The medical speciality of the doctor.
     */
    @NonNull
    private String speciality;

    /**
     * The address of the doctor.
     */
    @NonNull
    private String address;

    /**
     * The phone number of the doctor.
     */
    @NonNull
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
}
