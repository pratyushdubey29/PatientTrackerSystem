package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.NewCaseRequest;
import edu.pav.PatientTrackerSystem.commons.dto.UpdateCaseRequest;
import edu.pav.PatientTrackerSystem.commons.dto.UserTypeAndIDRequest;
import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.model.Case;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import edu.pav.PatientTrackerSystem.repository.CaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static edu.pav.PatientTrackerSystem.commons.Utils.*;

@RestController
public class CaseController {

    @Autowired
    CaseRepository caseRepository;

    @Autowired
    AppointmentController appointmentController;

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping(value = "/cases")
    public BaseResponse<List<Case>> getAllCases() {
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, caseRepository.findAll());
    }

    @GetMapping(value = "/cases/{id}")
    public BaseResponse getCaseById(@PathVariable("id") Long id) {
        Optional<Case> retrievedCase = caseRepository.findById(id);
        return retrievedCase.map(caseEntry -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, caseEntry))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.CASE_ID_NOT_FOUND_STRING, Case.builder().build()));
    }

    @GetMapping(value = "/cases/user-cases")
    public BaseResponse<List<Case>> getCaseByUser(@RequestBody UserTypeAndIDRequest request) {
        // TODO: Check if User with that Id exists or not; Discuss if required
        if (request.getUserType().equalsIgnoreCase(Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SUCCESS, caseRepository.findByPatientId(request.getId()));
        } else {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SUCCESS, caseRepository.findByDoctorId(request.getId()));
        }
    }

    @GetMapping(value = "/cases/open-cases")
    public BaseResponse openCasesPatientDoctorPair(@RequestParam Long patientId, @RequestParam Long doctorId){
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS,
                caseRepository.findByDoctorIdAndPatientIdAndCloseDateIsNull(doctorId, patientId));
    }

    @GetMapping(value = "/cases/monthly")
    public BaseResponse yearlyCases(@RequestParam Long doctorId, @RequestParam int year){
        List<Case> yearlyCases = caseRepository.findByDoctorIdAndOpenDateContaining(doctorId, Integer.toString(year));

        LinkedHashMap <String, Integer> casesByMonth = new LinkedHashMap<>();
        for (Month month : Month.values()) {
            casesByMonth.put(month.toString(), 0);
        }

        for (Case caseItem : yearlyCases) {
            LocalDate openingDate = LocalDate.parse(caseItem.getOpenDate());
            String monthKey = openingDate.getMonth().toString();
            casesByMonth.put(monthKey, casesByMonth.getOrDefault(monthKey, 0) + 1);
        }
        return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, casesByMonth);
    }

    @PostMapping(value = "/cases/create")
    @Transactional
    public BaseResponse createNewCase(@RequestBody NewCaseRequest request) {
        //Creates a new case and a new appointment together

        // TODO: Check if Users with those Ids exists or not; Discuss if required

        Long doctorId = request.getDoctorId();
        Long patientId = request.getPatientId();
        String requestedTime = request.getTime();
        String requestedDate = request.getDate();

        DateTimeFormatStatus status = dateTimeFormatAndPastCheck(requestedDate, requestedTime);
        if (status == DateTimeFormatStatus.ILL_FORMATTED){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        } else if (status == DateTimeFormatStatus.PAST_DATETIME){
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
        }

        Case newCase = Case.builder()
                .doctorId(doctorId).patientId(patientId)
                .openDate(getCurrentFormattedDate())
                .build();

        Case returnedCase = caseRepository.save(newCase);
        Appointment newAppointment = Appointment.builder()
                .caseId(returnedCase.getCaseId()).patientId(request.getPatientId())
                .doctorId(request.getDoctorId()).date(requestedDate).time(requestedTime)
                .build();

        BaseResponse response = appointmentController.bookAppointment(newAppointment);

        if (!Objects.equals(response.getMsg(), Constants.SUCCESS)){
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    @PostMapping(value = "cases/update")
    public BaseResponse updateCase(@RequestBody UpdateCaseRequest request){
        List<Case> retrievedCase = caseRepository.findByCaseIdAndCloseDateIsNull(request.getCaseId());
        if(retrievedCase.isEmpty()){
            return new BaseResponse(HttpStatus.NOT_FOUND, Constants.CASE_NOT_FOUND_OR_CLOSED_STRING, Case.builder().build());
        }
        retrievedCase.get(0).setSymptoms(request.getSymptoms());
        retrievedCase.get(0).setMedicines(request.getMedicines());
        retrievedCase.get(0).setCost(request.getCost());
        Case updatedCase = caseRepository.save(retrievedCase.get(0));
        return new BaseResponse(HttpStatus.OK, Constants.SUCCESS, updatedCase);
    }

    @PostMapping (value = "/cases/close")
    public BaseResponse closeCase(@RequestParam Long caseId, @RequestParam Long doctorId){
        List<Case> returnedCases = caseRepository.findByDoctorIdAndCaseIdAndCloseDateIsNull(doctorId, caseId);
        if (returnedCases.isEmpty()){
            return new BaseResponse(HttpStatus.NOT_FOUND, Constants.CASE_NOT_FOUND_OR_CLOSED_STRING, Case.builder().build());
        }
        else {
            String formattedCurrDate = getCurrentFormattedDate();
            returnedCases.get(0).setCloseDate(formattedCurrDate);
            Case updatedCase = caseRepository.save(returnedCases.get(0));
            return new BaseResponse(HttpStatus.OK, Constants.CASE_SUCCESSFULLY_CLOSED_STRING, updatedCase);
        }
    }

    //    @DeleteMapping(value = "/cases/{id}")
    private void deleteCase(@PathVariable("id") Long id) {
        if (id != null) {
            caseRepository.deleteById(id);
        }
    }
}
