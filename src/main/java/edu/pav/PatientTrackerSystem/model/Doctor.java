package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Doctors")
@Data
@NoArgsConstructor
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

    public Doctor(String dob, String name, String hospital, String speciality, String address, String phoneNumber, String email) {
        this.dob = dob;
        this.name = name;
        this.hospital = hospital;
        this.speciality = speciality;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
