package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.*;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtTokenUtil;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtUserDetailsService;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.DoctorsLogin;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.repository.DoctorRepository;
import edu.pav.PatientTrackerSystem.repository.DoctorSignupRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorSignupRepository doctorSignupRepository;

    @InjectMocks
    private DoctorController controller;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

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
    public void testSignupConflict() {
        DoctorSignupRequest signupRequest = DoctorSignupRequest.builder()
                .appendedEmail("doctor:johndoe@example.com")
                .password("password456").dob("1980-05-15")
                .name("John Doe").address("456 Oak St")
                .hospital("abcd")
                .speciality("pqrs")
                .phoneNumber("1234567890").build();

        DoctorsLogin login = DoctorsLogin.builder().username("johndoe@example.com").id(1L).build();

        Mockito.when(doctorSignupRepository.findByUsername("johndoe@example.com"))
                .thenReturn(login);

        Mockito.when(passwordEncoder.encode("password456")).thenReturn("password456");

        Mockito.when(doctorSignupRepository.save(Mockito.any()))
                .thenReturn(login);

        Mockito.when(doctorSignupRepository.save(Mockito.any())).thenReturn(null);

        BaseResponse response = controller.signup(signupRequest);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatus());
        Assert.assertEquals(Constants.DOCTOR_ALREADY_PRESENT_STRING, response.getMsg());
        Assert.assertEquals(DoctorsLogin.builder().build(), response.getBody());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder().appendedUsername("doctor:john@doe.com").password("pass").build();
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("john@doe.com", "pass",
                new ArrayList<>());
        Mockito.when(userDetailsService.loadUserByUsername("doctor:john@doe.com")).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any(), Mockito.eq(Constants.DOCTOR))).thenReturn("sampleToken");

        Mockito.when(doctorRepository.findByEmail("john@doe.com")).thenReturn(Doctor.builder().doctorId(1L).build());

        BaseResponse response = controller.login(request);
        Assertions.assertEquals(BaseResponse.builder().status(HttpStatus.OK).msg(Constants.SUCCESS).body(LoginResponse.builder().userId(1L).token("sampleToken").build()).build(), response);
    }

    @Test
    public void testSignupSuccess() {
        DoctorSignupRequest signupRequest = DoctorSignupRequest.builder()
                .appendedEmail("doctor:johndoe@example.com")
                .password("password456").dob("1980-05-15")
                .name("John Doe").address("456 Oak St")
                .hospital("abcd")
                .speciality("pqrs")
                .phoneNumber("1234567890").build();

        DoctorsLogin login = DoctorsLogin.builder().username("johndoe@example.com").id(1L).build();

        Mockito.when(doctorSignupRepository.findByUsername("johndoe@example.com"))
                .thenReturn(null);

        Mockito.when(passwordEncoder.encode("password456")).thenReturn("password456");

        Mockito.when(doctorSignupRepository.save(Mockito.any()))
                .thenReturn(login);

        Mockito.when(doctorRepository.save(Mockito.any())).thenReturn(null);

        BaseResponse response = controller.signup(signupRequest);

        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(Constants.SUCCESS, response.getMsg());
        Assert.assertEquals(DoctorsLogin.builder().id(1L).build(), response.getBody());
    }
}
