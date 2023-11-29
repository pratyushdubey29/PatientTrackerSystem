package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientSignupRepository extends JpaRepository<PatientsLogin, String> {
}
