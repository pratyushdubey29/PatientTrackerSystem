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
public class DoctorProfileEditRequest {
    /**
     * The id of the doctor.
     */
    @NonNull
    private Long doctorId;

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

