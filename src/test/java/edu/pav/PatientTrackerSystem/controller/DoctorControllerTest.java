package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorSignupRequest;
import edu.pav.PatientTrackerSystem.commons.dto.DoctorProfileEditRequest;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorSignupRepository doctorSignupRepository;

    @InjectMocks
    private DoctorController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDoctorById() {
        Long doctorId = 1L;
        Doctor mockDoctor = Doctor.builder()
                .doctorId(doctorId)
                .name("Dr. Smith")
                .email("drsmith@example.com")
                .address("456 Oak St")
                .hospital("City Hospital")
                .dob("1980-05-15")
                .speciality("Cardiology")
                .phoneNumber("9876543210")
                .isApproved(true)
                .build();

        Mockito.when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(mockDoctor));

        BaseResponse response = controller.getDoctorById(doctorId);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(mockDoctor, response.getBody());
    }

    @Test
    public void testEditProfile() {
        Long doctorId = 1L;
        DoctorProfileEditRequest editRequest = DoctorProfileEditRequest.builder()
                .doctorId(doctorId).hospital("New Hospital").speciality("Neurology")
                .address("789 Maple St").phoneNumber("1234567890").build();

        Doctor existingDoctor = Doctor.builder()
                .doctorId(doctorId)
                .address("456 Oak St")
                .hospital("City Hospital")
                .speciality("Cardiology")
                .phoneNumber("9876543210")
                .build();

        Mockito.when(doctorRepository.findById(doctorId))
                .thenReturn(Optional.of(existingDoctor));

        Mockito.when(doctorRepository.save(Mockito.any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BaseResponse response = controller.editProfile(editRequest);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(existingDoctor, response.getBody());
    }

    @Test
    public void testSignup() {
        DoctorSignupRequest signupRequest = DoctorSignupRequest.builder()
                .email("drbrown@example.com").password("password123").dob("1975-10-20").name("Dr. Brown")
                .hospital("General Hospital").speciality("Orthopedics").address("123 Elm St")
                .phoneNumber("5555555555").build();


        Mockito.when(doctorRepository.findByEmail(signupRequest.getEmail()))
                .thenReturn(null);

        Mockito.when(doctorRepository.save(Mockito.any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DoctorsLogin doctorsLogin = DoctorsLogin.builder()
                .loginKey(UserLoginKey.builder()
                        .userId(1L)
                        .userName(signupRequest.getEmail())
                        .build())
                .password(Utils.encryptPassword(signupRequest.getPassword()))
                .build();

        Mockito.when(doctorSignupRepository.save(Mockito.any(DoctorsLogin.class)))
                .thenReturn(doctorsLogin);

        BaseResponse response = controller.signup(signupRequest);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(Constants.SUCCESS, response.getBody());
    }
}
