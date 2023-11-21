package edu.pav.PatientTrackerSystem.repository;

import edu.pav.PatientTrackerSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.Doc;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialityContainingOrNameContainingOrAddressContaining(String speciality, String name, String address);

    @Query("SELECT d FROM Doctor d WHERE d.speciality LIKE CONCAT('%', :speciality, '%') " +
            "AND d.name LIKE CONCAT('%', :name,'%') " +
            "AND d.address LIKE CONCAT('%', :address,'%')")
    List<Doctor> find(String speciality, String name, String address);
}
