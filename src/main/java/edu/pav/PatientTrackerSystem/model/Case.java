package edu.pav.PatientTrackerSystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * Entity class representing a medical case.
 * This class is mapped to the "cases" table in the database.
 */
@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@SuperBuilder
public class Case {

    /**
     * Unique identifier for a medical case.
     */
    @Id
    @Column(name = "case_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    /**
     * Identifier of the patient associated with the case.
     */
    private Long patientId;

    /**
     * Identifier of the doctor associated with the case.
     */
    private Long doctorId;

    /**
     * Date when the case was opened.
     */
    private String openDate;

    /**
     * Date when the case was closed.
     */
    private String closeDate;

    /**
     * Symptoms reported in the medical case.
     */
    private String symptoms;

    /**
     * Medicines prescribed for the medical case.
     */
    private String medicines;

    /**
     * Cost associated with the medical case.
     */
    private Float cost;

    /**
     * Constructor to initialize a medical case with essential details.
     *
     * @param patientId Identifier of the patient associated with the case.
     * @param doctorId  Identifier of the doctor associated with the case.
     * @param openDate  Date when the case was opened.
     * @param closeDate Date when the case was closed.
     * @param symptoms  Symptoms reported in the medical case.
     * @param medicines Medicines prescribed for the medical case.
     * @param cost      Cost associated with the medical case.
     */
    public Case(Long patientId, Long doctorId, String openDate, String closeDate, String symptoms, String medicines, Float cost) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.symptoms = symptoms;
        this.medicines = medicines;
        this.cost = cost;
    }
}
