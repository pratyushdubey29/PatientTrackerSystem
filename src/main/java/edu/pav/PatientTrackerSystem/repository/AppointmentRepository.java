package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndDate(Long id, String date);
    List<Appointment> findByDoctorIdAndDateGreaterThanEqual(Long id, String date);
    List<Appointment> findByPatientIdAndDate(Long id, String date);
    List<Appointment> findByPatientIdAndDateGreaterThanEqual(Long id, String date);
}
