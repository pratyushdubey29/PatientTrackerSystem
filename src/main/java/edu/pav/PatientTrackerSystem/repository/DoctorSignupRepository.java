package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing doctor sign-up data in the database.
 * Extends JpaRepository for basic CRUD operations.
 *
 * @param <DoctorsLogin>      The entity type representing doctor sign-up data.
 * @param <UserLoginKey>      The key type for identifying the user in the repository.
 */
public interface DoctorSignupRepository extends JpaRepository<DoctorsLogin, UserLoginKey> {
}
