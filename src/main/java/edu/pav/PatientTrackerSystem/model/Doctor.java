package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Table(name = "Doctors")
@Getter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String DOB;

    private String name;

    private String hospital;

    private String specialty;

    private String address;

    private Long mobile;

    private String email;

    public Doctor(String DOB, String name, String hospital, String specialty, String address, Long mobile, String email) {
        this.DOB = DOB;
        this.name = name;
        this.hospital = hospital;
        this.specialty = specialty;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
