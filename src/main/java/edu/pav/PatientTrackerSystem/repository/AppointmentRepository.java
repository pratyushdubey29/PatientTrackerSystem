package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndDate(Long doctorId, String date);
    List<Appointment> findByDoctorIdAndDateGreaterThanEqual(Long doctorId, String date);
    List<Appointment> findByDoctorIdAndDateAndTime(Long doctorId, String date, String time);
    List<Appointment> findByPatientIdAndDate(Long patientId, String date);
    List<Appointment> findByPatientIdAndDateGreaterThanEqual(Long patientId, String date);
    List<Appointment> findByPatientIdAndDateAndTime(Long patientId, String date, String time);
}
