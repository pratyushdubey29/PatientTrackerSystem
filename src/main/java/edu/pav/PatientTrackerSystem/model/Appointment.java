package edu.pav.PatientTrackerSystem.model;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity class representing appointments in the system.
 * Each appointment is associated with a case, patient, and doctor.
 */
@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@SuperBuilder
public class Appointment {

    /**
     * Unique identifier for the appointment.
     */
    @Id
    @Column(name = "appointment_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    /**
     * Identifier for the medical case associated with the appointment.
     */
    private Long caseId;

    /**
     * Identifier for the patient associated with the appointment.
     */
    private Long patientId;

    /**
     * Identifier for the doctor associated with the appointment.
     */
    private Long doctorId;

    /**
     * Date of the appointment in string format.
     */
    private String date;

    /**
     * Time of the appointment in string format.
     */
    private String time;

    /**
     * Parameterized constructor for creating an appointment.
     *
     * @param caseId    Identifier for the medical case associated with the appointment.
     * @param patientId Identifier for the patient associated with the appointment.
     * @param doctorId  Identifier for the doctor associated with the appointment.
     * @param date      Date of the appointment.
     * @param time      Time of the appointment.
     */
    // TODO: Remove constructor if not needed
    public Appointment(Long caseId, Long patientId, Long doctorId, String date, String time) {
        this.caseId = caseId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }
}
