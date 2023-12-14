package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.*;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtTokenUtil;
import edu.pav.PatientTrackerSystem.commons.jwt.JwtUserDetailsService;
import edu.pav.PatientTrackerSystem.model.Doctor;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientSignupRepository patientSignupRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private PatientController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPatientById() {
        Long patientId = 1L;
        Patient mockPatient = Patient.builder()
                .patientId(patientId)
                .name("John Doe")
                .email("johndoe@example.com")
                .address("123 Main St")
                .dob("1990-01-01")
                .sex('M')
                .height(180)
                .weight(75)
                .phoneNumber("1234567890")
                .build();

        Mockito.when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(mockPatient));

        BaseResponse response = controller.getPatientById(patientId);

        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(Constants.SUCCESS, response.getMsg());
        Assert.assertEquals(mockPatient, response.getBody());
    }

    @Test
    public void testEditProfile() {
        Long patientId = 1L;
        PatientProfileEditRequest editRequest = PatientProfileEditRequest.builder().patientId(patientId).weight(70)
                .height(175).address("456 Elm St").phoneNumber("9876543210").build();

        Patient existingPatient = Patient.builder()
                .patientId(patientId)
                .address("123 Main St")
                .height(180)
                .weight(75)
                .phoneNumber("1234567890")
                .build();

        Mockito.when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(existingPatient));

        Mockito.when(patientRepository.save(Mockito.any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BaseResponse response = controller.editProfile(editRequest);

        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(Constants.SUCCESS, response.getMsg());
        Assert.assertEquals(existingPatient, response.getBody());
    }

    @Test
    public void testSignupConflict() {
        PatientSignupRequest signupRequest = PatientSignupRequest.builder()
                .appendedEmail("patient:johndoe@example.com")
                .password("password456").dob("1980-05-15")
                .name("John Doe").sex('M').weight(180).height(188).address("456 Oak St")
                .phoneNumber("1234567890").build();

        PatientsLogin login = PatientsLogin.builder().username("johndoe@example.com").id(1L).build();

        Mockito.when(patientSignupRepository.findByUsername("johndoe@example.com"))
                .thenReturn(login);

        Mockito.when(passwordEncoder.encode("password456")).thenReturn("password456");

        Mockito.when(patientSignupRepository.save(Mockito.any()))
                .thenReturn(login);

        Mockito.when(patientRepository.save(Mockito.any())).thenReturn(null);

        BaseResponse response = controller.signup(signupRequest);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatus());
        Assert.assertEquals(Constants.PATIENT_ALREADY_PRESENT_STRING, response.getMsg());
        Assert.assertEquals(PatientsLogin.builder().build(), response.getBody());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder().appendedUsername("patient:john@doe.com").password("pass").build();
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("john@doe.com", "pass",
                new ArrayList<>());
        Mockito.when(userDetailsService.loadUserByUsername("doctor:john@doe.com")).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any(), Mockito.eq(Constants.PATIENT))).thenReturn("sampleToken");

        Mockito.when(patientRepository.findByEmail("john@doe.com")).thenReturn(Patient.builder().patientId(1L).build());

        BaseResponse response = controller.login(request);
        Assertions.assertEquals(BaseResponse.builder().status(HttpStatus.OK).msg(Constants.SUCCESS).body(LoginResponse.builder().userId(1L).token("sampleToken").build()).build(), response);
    }

    @Test
    public void testSignupSuccess() {
        PatientSignupRequest signupRequest = PatientSignupRequest.builder()
                .appendedEmail("patient:johndoe@example.com")
                .password("password456").dob("1980-05-15")
                .name("John Doe").sex('M').weight(180).height(188).address("456 Oak St")
                .phoneNumber("1234567890").build();

        PatientsLogin login = PatientsLogin.builder().username("johndoe@example.com").id(1L).build();

        Mockito.when(patientSignupRepository.findByUsername("johndoe@example.com"))
                .thenReturn(null);

        Mockito.when(passwordEncoder.encode("password456")).thenReturn("password456");

        Mockito.when(patientSignupRepository.save(Mockito.any()))
                .thenReturn(login);

        Mockito.when(patientRepository.save(Mockito.any())).thenReturn(null);

        BaseResponse response = controller.signup(signupRequest);

        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(Constants.SUCCESS, response.getMsg());
        Assert.assertEquals(PatientsLogin.builder().id(1L).build(), response.getBody());
    }
}
