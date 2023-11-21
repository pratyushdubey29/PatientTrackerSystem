package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Table(name = "Patients")
public class Patient {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int height;

    private float weight;

    private String DOB;

    private String name;

    private char sex;

    private String email;

    private Long number;

    private String address;


    public Patient(int height, float weight, String DOB, String name, char sex, String email, Long number, String address)
    {
        this.height = height;
        this.weight = weight;
        this.DOB = DOB;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.number = number;
        this.address = address;

    }
}
