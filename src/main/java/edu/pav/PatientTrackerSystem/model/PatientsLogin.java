package edu.pav.PatientTrackerSystem.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "patients_login")
public class PatientsLogin extends UserLogin{
}
