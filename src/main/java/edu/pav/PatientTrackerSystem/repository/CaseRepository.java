package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Long> {
    List<Case> findByPatientId(Long id);
    List<Case> findByDoctorId(Long id);
}
