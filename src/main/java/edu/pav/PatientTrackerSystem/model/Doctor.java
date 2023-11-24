package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@SuperBuilder
public class Doctor {

    @Id
    @Column(name = "doctor_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    private String dob;

    private String name;

    private String hospital;

    private String speciality;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "is_approved")
    private Boolean isApproved;

}
