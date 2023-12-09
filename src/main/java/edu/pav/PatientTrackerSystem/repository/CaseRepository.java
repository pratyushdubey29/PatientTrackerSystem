package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Case entities.
 */
public interface CaseRepository extends JpaRepository<Case, Long> {

    /**
     * Retrieve a list of cases associated with a specific patient.
     *
     * @param id The ID of the patient.
     * @return List of cases associated with the specified patient.
     */
    List<Case> findByPatientId(Long id);

    /**
     * Retrieve a list of cases associated with a specific doctor.
     *
     * @param id The ID of the doctor.
     * @return List of cases associated with the specified doctor.
     */
    List<Case> findByDoctorId(Long id);

    /**
     * Retrieve a list of open cases associated with a specific doctor and patient.
     *
     * @param doctorId The ID of the doctor.
     * @param patientId The ID of the patient.
     * @return List of open cases associated with the specified doctor and patient.
     */
    List<Case> findByDoctorIdAndPatientIdAndCloseDateIsNull(Long doctorId, Long patientId);

    /**
     * Retrieve a list of open cases associated with a specific doctor and case ID.
     *
     * @param doctorId The ID of the doctor.
     * @param caseId The ID of the case.
     * @return List of open cases associated with the specified doctor and case ID.
     */
    List<Case> findByDoctorIdAndCaseIdAndCloseDateIsNull(Long doctorId, Long caseId);

    /**
     * Retrieve a list of cases associated with a specific doctor for a given year.
     *
     * @param doctorId The ID of the doctor.
     * @param year The year to filter open cases.
     * @return List of cases associated with the specified doctor for the given year.
     */
    List<Case> findByDoctorIdAndOpenDateContaining(Long doctorId, String year);

    /**
     * Retrieve a list of open cases based on the case ID.
     *
     * @param caseId The ID of the case.
     * @return List of open cases with the specified case ID.
     */
    List<Case> findByCaseIdAndCloseDateIsNull(Long caseId);
}
