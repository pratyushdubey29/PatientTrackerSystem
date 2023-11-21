package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.*;
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

    public Doctor(String dob, String name, String hospital, String speciality, String address, String phoneNumber, String email, Boolean isApproved) {
        this.dob = dob;
        this.name = name;
        this.hospital = hospital;
        this.speciality = speciality;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isApproved = isApproved;
    }
}
