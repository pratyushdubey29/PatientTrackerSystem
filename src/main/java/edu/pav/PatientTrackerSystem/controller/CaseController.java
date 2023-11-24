package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.NewCaseRequest;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class CaseController {

    @Autowired
    CaseRepository caseRepository;

    @Autowired
    AppointmentController appointmentController;

    @Autowired
    AppointmentRepository appointmentRepository;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD_STRING);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.HH_mm_STRING);

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

    @PostMapping(value = "/cases/create")
    @Transactional
    public BaseResponse createNewCase(@RequestBody NewCaseRequest request) {
        //Creates a new case and a new appointment together

        // TODO: Check if Users with those Ids exists or not; Discuss if required
        // TODO: Move Time format and future/past checks (from everywhere) to some helper/util class.

        String formattedCurrDate = LocalDate.now().format(dateFormatter);
        String formattedCurrTime = LocalTime.now().format(timeFormatter);
        Long doctorId = request.getDoctorId();
        Long patientId = request.getPatientId();
        String date = request.getDate();
        String time = request.getTime();

        // If requested date is not formatted well
        if (!appointmentController.isValidDate(date) || !appointmentController.isValidTime(time)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        }

        // If requested date is in the past
        if (appointmentController.isFutureDatetime(formattedCurrDate, formattedCurrTime, date, time)) {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
        }

        Case newCase = Case.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .openDate(formattedCurrDate)
                .build();

        Case returnedCase = caseRepository.save(newCase);

        AppointmentController.AvailabilityStatus status = appointmentController.
                availabilityCheck(doctorId, patientId, date, time);

        if (status == AppointmentController.AvailabilityStatus.DOCTOR_BOOKED) {

            // If the doctor is booked at the requested time
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, Appointment.builder().build());
        } else if (status == AppointmentController.AvailabilityStatus.PATIENT_BOOKED) {

            // If the patient is booked at the requested time
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.PATIENT, Appointment.builder().build());
        } else {

            // Book an appointment
            Appointment newAppointment = Appointment.builder()
                    .caseId(returnedCase.getCaseId())
                    .patientId(patientId)
                    .doctorId(doctorId)
                    .date(date)
                    .time(time)
                    .build();
            Appointment returnedAppointment = appointmentRepository.save(newAppointment);
            return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, returnedAppointment);
        }
    }


    //    @DeleteMapping(value = "/cases/{id}")
    private void deleteCase(@PathVariable("id") Long id) {
        if (id != null) {
            caseRepository.deleteById(id);
        }
    }
}
