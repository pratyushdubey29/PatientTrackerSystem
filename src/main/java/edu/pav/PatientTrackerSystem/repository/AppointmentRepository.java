package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Appointment entities.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Retrieves a list of appointments for a specific doctor on a given date.
     *
     * @param doctorId The ID of the doctor.
     * @param date     The date for which appointments are to be retrieved.
     * @return A list of appointments for the specified doctor and date.
     */
    List<Appointment> findByDoctorIdAndDate(Long doctorId, String date);

    /**
     * Retrieves a list of appointments for a specific doctor on a given date and time.
     *
     * @param doctorId The ID of the doctor.
     * @param date     The date for which appointments are to be retrieved.
     * @param time     The time for which appointments are to be retrieved.
     * @return A list of appointments for the specified doctor, date, and time.
     */
    List<Appointment> findByDoctorIdAndDateAndTime(Long doctorId, String date, String time);

    /**
     * Retrieves a list of appointments for a specific patient on a given date.
     *
     * @param patientId The ID of the patient.
     * @param date      The date for which appointments are to be retrieved.
     * @return A list of appointments for the specified patient and date.
     */
    List<Appointment> findByPatientIdAndDate(Long patientId, String date);

    /**
     * Retrieves a list of appointments for a specific patient on a given date and time.
     *
     * @param patientId The ID of the patient.
     * @param date      The date for which appointments are to be retrieved.
     * @param time      The time for which appointments are to be retrieved.
     * @return A list of appointments for the specified patient, date, and time.
     */
    List<Appointment> findByPatientIdAndDateAndTime(Long patientId, String date, String time);

    /**
     * Retrieves a list of appointments for a specific doctor.
     *
     * @param id The ID of the doctor.
     * @return A list of appointments for the specified doctor.
     */
    List<Appointment> findByDoctorId(Long id);

    /**
     * Retrieves a list of appointments for a specific patient.
     *
     * @param id The ID of the patient.
     * @return A list of appointments for the specified patient.
     */
    List<Appointment> findByPatientId(Long id);
}
