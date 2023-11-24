package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@SuperBuilder
public class NewAppointmentRequest {

    @NonNull
    private Long caseId;
    @NonNull
    private Long patientId;
    @NonNull
    private Long doctorId;
    @NonNull
    private String date;
    @NonNull
    private String time;

}