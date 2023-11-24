package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PatientRepository extends JpaRepository<Patient, Long>{
    Patient findByEmail(String email);
}
