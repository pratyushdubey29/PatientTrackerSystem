package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@SuperBuilder
public class Case {

    @Id
    @Column(name = "case_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    private Long patientId;

    private Long doctorId;

    private String openDate;

    private String closeDate;

    private String symptoms;

    private String medicines;

    private Float cost;

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
