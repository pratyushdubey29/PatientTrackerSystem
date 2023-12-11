package edu.pav.PatientTrackerSystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * Entity class representing a patient in the system.
 * This class is mapped to the "patients" table in the database.
 */
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@SuperBuilder
public class Patient {

    /**
     * Unique identifier for the patient.
     */
    @Id
    @Column(name = "patient_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    /**
     * Date of birth of the patient in the format "YYYY-MM-DD".
     */
    private String dob;

    /**
     * Name of the patient.
     */
    private String name;

    /**
     * Height of the patient in centimeters.
     */
    private int height;

    /**
     * Weight of the patient in kilograms.
     */
    private int weight;

    /**
     * Gender of the patient, represented as a single character.
     */
    private char sex;

    /**
     * Address of the patient.
     */
    private String address;

    /**
     * Phone number of the patient.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Email address of the patient.
     */
    private String email;
}
