package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request for creating a new medical case.
 * This class is used to encapsulate the necessary information for creating a new case.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class NewCaseRequest {

    /**
     * The unique identifier of the patient associated with the case.
     */
    @NonNull
    private Long patientId;

    /**
     * The unique identifier of the doctor responsible for the case.
     */
    @NonNull
    private Long doctorId;

    /**
     * The time of the case, specified as a string.
     */
    @NonNull
    private String time;

    /**
     * The date of the case, specified as a string.
     */
    @NonNull
    private String date;
}
