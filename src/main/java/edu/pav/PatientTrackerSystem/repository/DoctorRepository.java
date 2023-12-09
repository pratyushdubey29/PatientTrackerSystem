package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Javadoc comments for the DoctorRepository interface.
 * This interface extends JpaRepository to handle Doctor entity operations.
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Finds a list of doctors based on specified criteria.
     *
     * @param speciality The speciality of the doctors to search for.
     * @param name       The name of the doctors to search for.
     * @param address    The address of the doctors to search for.
     * @return A list of {@code Doctor} entities matching the specified criteria.
     */
    @Query("SELECT d FROM Doctor d WHERE d.speciality LIKE CONCAT('%', :speciality, '%') " +
            "AND d.name LIKE CONCAT('%', :name,'%') " +
            "AND d.address LIKE CONCAT('%', :address,'%')")
    List<Doctor> find(String speciality, String name, String address);

    /**
     * Finds a doctor by email.
     *
     * @param email The email of the doctor to search for.
     * @return The {@code Doctor} entity with the specified email.
     */
    Doctor findByEmail(String email);
}
