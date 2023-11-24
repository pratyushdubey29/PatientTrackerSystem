package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@SuperBuilder
public class Patient {

    @Id
    @Column(name = "patient_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private String dob;

    private String name;

    private int height;

    private int weight;

    private char sex;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
}
