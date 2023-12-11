package edu.pav.PatientTrackerSystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * Represents a Doctor entity in the system.
 * This class is mapped to the "doctors" table in the database.
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@SuperBuilder
public class Doctor {

    /**
     * Unique identifier for a doctor.
     */
    @Id
    @Column(name = "doctor_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    /**
     * Date of birth of the doctor.
     */
    private String dob;

    /**
     * Name of the doctor.
     */
    private String name;

    /**
     * Affiliated hospital of the doctor.
     */
    private String hospital;

    /**
     * Medical specialty(s) of the doctor.
     */
    private String speciality;

    /**
     * Address of the doctor.
     */
    private String address;

    /**
     * Phone number of the doctor.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Email address of the doctor.
     */
    private String email;

    /**
     * Approval status of the doctor.
     */
    @Column(name = "is_approved")
    private Boolean isApproved;

}
