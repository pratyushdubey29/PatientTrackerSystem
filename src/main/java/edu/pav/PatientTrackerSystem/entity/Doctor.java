package edu.pav.PatientTrackerSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long doctorId;

    @Column
    private String DOB;

    @Column
    private String name;

    @Column
    private String hospital;

    @Column
    private String specialty;

    @Column
    private String address;

    @Column
    private Long mobile;

    @Column
    private String email;

}
