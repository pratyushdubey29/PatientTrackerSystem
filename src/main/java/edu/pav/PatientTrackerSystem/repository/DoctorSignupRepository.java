package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorSignupRepository extends JpaRepository<DoctorsLogin, UserLoginKey> {
}
