package edu.pav.PatientTrackerSystem.commons.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class RescheduleAppointmentRequest {

    @NonNull
    private Long appointmentId;
    @NonNull
    private String newDate;
    @NonNull
    private String newTime;

}