package edu.pav.PatientTrackerSystem.commons.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request to reschedule an appointment.
 * This class is part of the data model for handling appointment rescheduling.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class RescheduleAppointmentRequest {

    /**
     * The unique identifier for the appointment to be rescheduled.
     */
    @NonNull
    private Long appointmentId;

    /**
     * The new date for the rescheduled appointment.
     */
    @NonNull
    private String newDate;

    /**
     * The new time for the rescheduled appointment.
     */
    @NonNull
    private String newTime;

}