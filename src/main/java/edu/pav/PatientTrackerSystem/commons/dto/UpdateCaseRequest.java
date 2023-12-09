package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request to update a medical case with specific information.
 * This class is used to encapsulate the details needed for updating a case.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class UpdateCaseRequest {

    /**
     * The unique identifier for the medical case to be updated.
     */
    @NonNull
    private Long caseId;

    /**
     * The new symptoms information to be associated with the medical case.
     * May be null if no changes are required.
     */
    private String symptoms;

    /**
     * The new medicines information to be associated with the medical case.
     * May be null if no changes are required.
     */
    private String medicines;

    /**
     * The updated cost information for the medical case.
     * May be null if no changes are required.
     */
    private Float cost;

}
