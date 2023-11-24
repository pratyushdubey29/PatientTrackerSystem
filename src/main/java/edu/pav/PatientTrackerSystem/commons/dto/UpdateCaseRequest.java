package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class UpdateCaseRequest {

    @NonNull
    private Long caseId;
    private String symptoms;
    private String medicines;
    private Float cost;

}
