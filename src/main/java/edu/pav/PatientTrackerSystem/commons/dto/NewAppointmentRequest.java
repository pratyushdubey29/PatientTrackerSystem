package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;


/**
 * Represents a request for a new appointment.
 *
 * This class encapsulates the details required to schedule a new appointment between a patient and a doctor.
 * The information includes the case ID, patient ID, doctor ID, date, and time for the appointment.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class NewAppointmentRequest {

    /**
     * The unique identifier for the medical case associated with the appointment.
     */
    @NonNull    
    private Long caseId;

    /**
     * The unique identifier for the patient involved in the appointment.
     */
    @NonNull
    private Long patientId;

    /**
     * The unique identifier for the doctor involved in the appointment.
     */
    @NonNull
    private Long doctorId;

    /**
     * The date on which the appointment is scheduled.
     */
    @NonNull
    private String date;

    /**
     * The time at which the appointment is scheduled.
     */
    @NonNull
    private String time;

}