package edu.pav.PatientTrackerSystem.repository;

        import edu.pav.PatientTrackerSystem.model.Case;
        import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, Long> {
}
