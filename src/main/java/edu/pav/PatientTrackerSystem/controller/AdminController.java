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

@RestController
public class AdminController {

    @Autowired
    DoctorController doctorController;

    @Autowired
    DoctorRepository doctorRepository;


    @PostMapping(value = "/admin/approve-doctor/{id}")
    private BaseResponse approveDoctor(@PathVariable("id") Long id){
        BaseResponse searchResponse = doctorController.getDoctorById(id);
        if (Objects.equals(searchResponse.getMsg(), Constants.DOCTOR_ID_NOT_FOUND_STRING)){
            return searchResponse;
        } else {
            Doctor retrievedDoctor = (Doctor) searchResponse.getBody();
            retrievedDoctor.setIsApproved(true);
            Doctor approvedDoctor = doctorRepository.save(retrievedDoctor);
            return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, approvedDoctor);
        }
    }
}
