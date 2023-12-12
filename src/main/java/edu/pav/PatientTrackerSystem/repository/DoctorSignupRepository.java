package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing doctor sign-up data in the database.
 * Extends JpaRepository for basic CRUD operations.
 *
 */
public interface DoctorSignupRepository extends JpaRepository<DoctorsLogin, Integer> {
    DoctorsLogin findByUsername(String username);
}
