package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Patient repository.
 * This repository is responsible for managing Patient entities in the database.
 */
public interface PatientRepository extends JpaRepository<Patient, Long>{
    Patient findByEmail(String email);
}
