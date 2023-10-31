package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
