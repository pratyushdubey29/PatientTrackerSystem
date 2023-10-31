package edu.pav.PatientTrackerSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long patientId;

    @Column(name="height")
    private Integer height;

    @Column(name="weight")
    private Integer weight;

    @Column(name="dob")
    private String DOB;

    @Column(name="name")
    private String name;

    @Column(name="sex")
    private String sex;

    @Column(name="email")
    private String email;

    @Column(name="phone_number")
    private Long phoneNumber;

    @Column(name="address")
    private String address;
}
