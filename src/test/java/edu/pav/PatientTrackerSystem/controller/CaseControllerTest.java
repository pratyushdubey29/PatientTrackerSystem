package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.EmailService;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.UserTypeAndIDRequest;
import edu.pav.PatientTrackerSystem.model.Case;
import edu.pav.PatientTrackerSystem.repository.CaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CaseControllerTest {

    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private CaseController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCases() {
        Case mockCase = Case.builder()
                .caseId(1L)
                .patientId(101L)
                .doctorId(201L)
                .openDate("2023-01-01")
                .closeDate(null)
                .symptoms("Fever")
                .medicines("Paracetamol")
                .cost(50.0f)
                .build();

        List<Case> mockCases = new ArrayList<>();
        mockCases.add(mockCase);

        when(caseRepository.findAll()).thenReturn(mockCases);

        BaseResponse<List<Case>> response = controller.getAllCases();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(mockCases, response.getBody());
    }

    @Test
    public void testGetCaseByUser() {
        UserTypeAndIDRequest patientRequest = new UserTypeAndIDRequest(Constants.PATIENT, 101L);

        Case patientCase1 = Case.builder()
                .caseId(1L)
                .patientId(101L)
                .doctorId(201L)
                .build();

        Case patientCase2 = Case.builder()
                .caseId(2L)
                .patientId(101L)
                .doctorId(202L)
                .build();

        List<Case> patientCases = Arrays.asList(patientCase1, patientCase2);

        when(caseRepository.findByPatientId(patientRequest.getId())).thenReturn(patientCases);

        UserTypeAndIDRequest doctorRequest = new UserTypeAndIDRequest(Constants.DOCTOR, 201L);

        Case doctorCase = Case.builder()
                .caseId(3L)
                .patientId(102L)
                .doctorId(201L)
                .build();

        List<Case> doctorCases = Arrays.asList(patientCase1, doctorCase);

        when(caseRepository.findByDoctorId(doctorRequest.getId())).thenReturn(doctorCases);

        BaseResponse<List<Case>> response = controller.getCaseByUser(patientRequest);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Constants.SUCCESS, response.getMsg());
        assertEquals(patientCases, response.getBody());

        BaseResponse<List<Case>> doctorResponse = controller.getCaseByUser(doctorRequest);

        assertEquals(HttpStatus.OK, doctorResponse.getStatus());
        assertEquals(Constants.SUCCESS, doctorResponse.getMsg());
        assertEquals(doctorCases, doctorResponse.getBody());
    }


}
