package edu.pav.PatientTrackerSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "patients_login")
public class PatientsLogin extends UserLogin{
}
