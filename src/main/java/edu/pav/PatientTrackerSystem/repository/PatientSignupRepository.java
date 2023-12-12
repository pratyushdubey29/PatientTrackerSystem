package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing patient sign-up data in the database.
 * Extends JpaRepository for basic CRUD operations.
 */
public interface PatientSignupRepository extends JpaRepository<PatientsLogin, Integer> {
    PatientsLogin findByUsername(String username);

}
