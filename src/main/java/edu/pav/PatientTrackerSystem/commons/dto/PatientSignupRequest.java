package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request object for patient signup.
 * This class is used to encapsulate the information required for a patient to sign up in the system.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class PatientSignupRequest {

    /**
     * The email address of the patient.
     */
    @NonNull
    private String appendedEmail;

    /**
     * The password chosen by the patient for authentication.
     */
    @NonNull
    private String password;

    /**
     * The date of birth of the patient.
     */
    @NonNull
    private String dob;

    /**
     * The name of the patient.
     */
    @NonNull
    private String name;

    /**
     * The gender of the patient (represented as a single character).
     */
    @NonNull
    private char sex;

    /**
     * The weight of the patient in kilograms.
     */
    @NonNull
    private int weight;

    /**
     * The height of the patient in centimeters.
     */
    @NonNull
    private int height;

    /**
     * The address of the patient.
     */
    @NonNull
    private String address;

    /**
     * The phone number of the patient.
     */
    @NonNull
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
}
