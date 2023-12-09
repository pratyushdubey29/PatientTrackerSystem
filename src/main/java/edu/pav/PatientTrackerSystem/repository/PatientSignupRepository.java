package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing patient sign-up data in the database.
 * Extends JpaRepository for basic CRUD operations.
 *
 * @param <DoctorsLogin>      The entity type representing patient sign-up data.
 * @param <UserLoginKey>      The key type for identifying the user in the repository.
 */
public interface PatientSignupRepository extends JpaRepository<PatientsLogin, UserLoginKey> {
}
