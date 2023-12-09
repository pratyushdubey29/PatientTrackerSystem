package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * The AdminController class handles the approval of doctor registration by the admin.
 * It communicates with the DoctorController and DoctorRepository for necessary operations.
 */
@RestController
public class AdminController {

    /**
     * The DoctorController instance for handling doctor-related operations.
     */
    @Autowired
    DoctorController doctorController;

    /**
     * The DoctorRepository instance for interacting with the database.
     */
    @Autowired
    DoctorRepository doctorRepository;

    /**
     * Approves the registration of a doctor with the specified ID.
     *
     * @param id The ID of the doctor to be approved.
     * @return A BaseResponse containing the result of the approval process.
     *         If successful, it includes the approved Doctor entity.
     *         If the doctor with the given ID is not found, an error message is provided.
     */
    @PostMapping(value = "/admin/approve-doctor/{id}")
    private BaseResponse approveDoctor(@PathVariable("id") Long id){
        // Check if the doctor with the given ID exists
        BaseResponse searchResponse = doctorController.getDoctorById(id);
        if (Objects.equals(searchResponse.getMsg(), Constants.DOCTOR_ID_NOT_FOUND_STRING)){
            return searchResponse; // Doctor not found, return error response
        } else {
            Doctor retrievedDoctor = (Doctor) searchResponse.getBody(); 
            // Set the approval status to true
            retrievedDoctor.setIsApproved(true);
            // Save the updated doctor entity in the database
            Doctor approvedDoctor = doctorRepository.save(retrievedDoctor);
            return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, approvedDoctor);
        }
    }
}
