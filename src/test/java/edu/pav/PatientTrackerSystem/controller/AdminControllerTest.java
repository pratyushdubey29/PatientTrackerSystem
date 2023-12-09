package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;

import javax.print.Doc;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {
    @Mock
    private DoctorController doctorController;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AdminController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
    }

    @Test
    public void testApproveDoctorSuccess() {

        Doctor doctor = Doctor.builder()
                .doctorId(1L)
                .isApproved(Boolean.FALSE)
                .build();

        Mockito.when(doctorController.getDoctorById(1L))
                .thenReturn(BaseResponse.<Doctor>builder()
                        .body(doctor).build());

        Mockito.when(doctorRepository.save(doctor))
                .thenReturn(doctor);

        BaseResponse response = controller.approveDoctor(1L);

        Assertions.assertEquals(BaseResponse.builder()
                .status(HttpStatus.OK)
                .msg(Constants.SUCCESS)
                .body(doctor)
                .build(), response);
    }

    @Test
    public void testApproveDoctorDoctorAbsent() {

        BaseResponse notFoundResponse = BaseResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .body(Doctor.builder().build())
                .msg(Constants.DOCTOR_ID_NOT_FOUND_STRING)
                .build();

        Mockito.when(doctorController.getDoctorById(2L))
                .thenReturn(notFoundResponse);

        Assertions.assertEquals(notFoundResponse, controller.approveDoctor(2L));
    }
}