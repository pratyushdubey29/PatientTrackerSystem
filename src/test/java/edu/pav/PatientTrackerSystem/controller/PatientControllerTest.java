package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.Utils;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.PatientProfileEditRequest;
import edu.pav.PatientTrackerSystem.commons.dto.PatientSignupRequest;
import edu.pav.PatientTrackerSystem.model.Patient;
import edu.pav.PatientTrackerSystem.model.PatientsLogin;
import edu.pav.PatientTrackerSystem.model.UserLoginKey;
import edu.pav.PatientTrackerSystem.repository.PatientRepository;
import edu.pav.PatientTrackerSystem.repository.PatientSignupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientSignupRepository patientSignupRepository;

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
    public void testSignup() {
        PatientSignupRequest signupRequest = PatientSignupRequest.builder()
                .email("johndoe@example.com").password("password456").dob("1980-05-15")
                .name("John Doe").sex('M').weight(180).height(188).address("456 Oak St")
                .phoneNumber("1234567890").build();
        Mockito.when(patientRepository.findByEmail(signupRequest.getEmail()))
                .thenReturn(null);

        Mockito.when(patientRepository.save(Mockito.any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PatientsLogin patientsLogin = PatientsLogin.builder()
                .loginKey(UserLoginKey.builder()
                        .userId(1L)
                        .userName(signupRequest.getEmail())
                        .build())
                .password(Utils.encryptPassword(signupRequest.getPassword()))
                .build();

        Mockito.when(patientSignupRepository.save(Mockito.any(PatientsLogin.class)))
                .thenReturn(patientsLogin);

        BaseResponse response = controller.signup(signupRequest);

        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(Constants.SUCCESS, response.getMsg());
        Assert.assertEquals(Constants.SUCCESS, response.getBody());
    }
}
