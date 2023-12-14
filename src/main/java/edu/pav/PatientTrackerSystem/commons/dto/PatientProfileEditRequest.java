package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request object for doctor profile edit.
 * This class is used to encapsulate the information required for a doctor to edit its profile in the system.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class PatientProfileEditRequest {

    /**
     * The id of the patient.
     */
    @NonNull
    private Long patientId;

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
