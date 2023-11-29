package edu.pav.PatientTrackerSystem.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "doctors_login")
public class DoctorsLogin extends UserLogin{
}
