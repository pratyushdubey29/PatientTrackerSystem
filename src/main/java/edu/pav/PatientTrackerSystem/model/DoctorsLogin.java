package edu.pav.PatientTrackerSystem.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents the entity class for storing login information specific to doctors.
 * Extends the UserLogin class, inheriting common login attributes.
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "doctors_login")
public class DoctorsLogin extends UserLogin{
}
