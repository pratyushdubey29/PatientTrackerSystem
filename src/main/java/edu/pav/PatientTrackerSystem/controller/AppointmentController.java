package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.CreateAppointmentRequest;
import edu.pav.PatientTrackerSystem.commons.dto.FetchAppointmentRequest;
import edu.pav.PatientTrackerSystem.commons.dto.RescheduleAppointmentRequest;
import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD_STRING);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.HH_mm_STRING);

    @GetMapping("/appointments")
    public BaseResponse<List<Appointment>> getAllAppointments() {
        return new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, appointmentRepository.findAll());
    }

    @GetMapping(value = "/appointments/{id}")
    public BaseResponse<Appointment> getAppointmentById(@PathVariable("id") Long id) {
        Optional<Appointment> retrievedAppointment = appointmentRepository.findById(id);
        return retrievedAppointment.map(appointment -> new BaseResponse<>(HttpStatus.OK, Constants.SUCCESS, appointment))
                .orElseGet(() -> new BaseResponse<>(HttpStatus.NOT_FOUND,
                        Constants.APPOINTMENT_ID_NOT_FOUND_STRING, Appointment.builder().build()));
    }

    @PostMapping(value = "/appointments/view-today")
    public BaseResponse getTodaysAppointments(@RequestBody FetchAppointmentRequest request) {

        String formattedCurrDate = LocalDate.now().format(dateFormatter);

        String userType = request.getUserType();
        if (!Objects.equals(userType.toLowerCase(), Constants.DOCTOR) &&
                !Objects.equals(userType.toLowerCase(), Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_USER_TYPE_STRING, Appointment.builder().build());
        }

        List<Appointment> retrievedAppointments;
        if (Objects.equals(request.getUserType().toLowerCase(), Constants.DOCTOR)) {
            retrievedAppointments = appointmentRepository.findByDoctorIdAndDate(request.getId(), formattedCurrDate);
        } else {
            retrievedAppointments = appointmentRepository.findByPatientIdAndDate(request.getId(), formattedCurrDate);
        }
        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENT_FOUND_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }

    @PostMapping(value = "/appointments/view-future")
    public BaseResponse getFutureAppointments(@RequestBody FetchAppointmentRequest request) {

        String formattedCurrDate = LocalDate.now().format(dateFormatter);
        String formattedCurrTime = LocalTime.now().format(timeFormatter);

        String userType = request.getUserType();
        if (!Objects.equals(userType.toLowerCase(), Constants.DOCTOR) &&
                !Objects.equals(userType.toLowerCase(), Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_USER_TYPE_STRING, Appointment.builder().build());
        }

        List<Appointment> retrievedAppointments;
        if (Objects.equals(request.getUserType().toLowerCase(), Constants.DOCTOR)) {
            retrievedAppointments = filterPastOut(appointmentRepository.findByDoctorIdAndDateGreaterThanEqual(
                    request.getId(), formattedCurrDate), formattedCurrDate, formattedCurrTime);
        } else {
            retrievedAppointments = filterPastOut(appointmentRepository.findByPatientIdAndDateGreaterThanEqual(
                    request.getId(), formattedCurrDate), formattedCurrDate, formattedCurrTime);
        }

        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENTS_FUTURE_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }


    @PostMapping(value = "appointments/reschedule")
    public BaseResponse getRescheduleFutureAppointments(@RequestBody RescheduleAppointmentRequest request) {

        String newTime = request.getNewTime();
        String newDate = request.getNewDate();
        Long appointmentId = request.getAppointmentId();

        String formattedCurrDate = LocalDate.now().format(dateFormatter);
        String formattedCurrTime = LocalTime.now().format(timeFormatter);

        // If requested date is not formatted well
        if (!isValidDate(newDate) || !isValidTime(newTime)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        }

        // If requested date is in the past
        if (isFutureDatetime(formattedCurrDate, formattedCurrTime, newDate, newTime)) {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
        }

        Optional<Appointment> currentAppointmentResponse = appointmentRepository.findById(appointmentId);

        // If appointment does not exist
        if (currentAppointmentResponse.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND,
                    Constants.NO_APPOINTMENT_FOUND_STRING, Appointment.builder().build());
        }
        Appointment currentAppointment = currentAppointmentResponse.get();

        // If the appointment is already completed in the past
        if (isFutureDatetime(formattedCurrDate, formattedCurrTime,
                currentAppointment.getDate(), currentAppointment.getTime())) {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.CANNOT_EDIT_PAST_APPOINTMENT, Appointment.builder().build());
        }

        AvailabilityStatus availabilityStatus = availabilityCheck(currentAppointment.getDoctorId(),
                currentAppointment.getPatientId(), newDate, newTime);

        if (availabilityStatus == AvailabilityStatus.DOCTOR_BOOKED) {

            // If the doctor is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, Appointment.builder().build());
        } else if (availabilityStatus == AvailabilityStatus.PATIENT_BOOKED) {

            // If the patient is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.PATIENT, Appointment.builder().build());
        } else {

            // Book an appointment
            Appointment newAppointment = Appointment.builder()
                    .appointmentId(appointmentId)
                    .caseId(currentAppointment.getCaseId())
                    .patientId(currentAppointment.getPatientId())
                    .doctorId(currentAppointment.getDoctorId())
                    .date(newDate)
                    .time(newTime)
                    .build();
            appointmentRepository.save(newAppointment);
            return new BaseResponse<>(HttpStatus.OK, Constants.RESCHEDULE_SUCCESSFUL, newAppointment);
        }
    }

    @PostMapping(value = "/appointments/schedule")
    public BaseResponse createAppointment(@RequestBody CreateAppointmentRequest request){
        String requestedTime = request.getTime();
        String requestedDate = request.getDate();

        String formattedCurrDate = LocalDate.now().format(dateFormatter);
        String formattedCurrTime = LocalTime.now().format(timeFormatter);

        // If requested date is not formatted well
        if (!isValidDate(requestedDate) || !isValidTime(requestedTime)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        }

        // If requested date is in the past
        if (isFutureDatetime(formattedCurrDate, formattedCurrTime, requestedDate, requestedTime)) {
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
        }

        AvailabilityStatus availabilityStatus = availabilityCheck(request.getDoctorId(),
                request.getPatientId(), requestedDate, requestedTime);

        if (availabilityStatus == AvailabilityStatus.DOCTOR_BOOKED) {

            // If the doctor is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, Appointment.builder().build());
        } else if (availabilityStatus == AvailabilityStatus.PATIENT_BOOKED) {

            // If the patient is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.PATIENT, Appointment.builder().build());
        } else {

            // Book an appointment
            Appointment newAppointment = Appointment.builder()
                    .caseId(request.getCaseId())
                    .patientId(request.getPatientId())
                    .doctorId(request.getDoctorId())
                    .date(requestedDate)
                    .time(requestedTime)
                    .build();
            appointmentRepository.save(newAppointment);
            return new BaseResponse<>(HttpStatus.OK, Constants.SCHEDULE_APPOINTMENT_SUCCESSFUL, newAppointment);
        }
    }

//    @DeleteMapping(value = "/appointments/{id}")
    private void deleteAppointment(@PathVariable("id") Long id) {
        if (id != null) {
            appointmentRepository.deleteById(id);
        }
    }


    private List<Appointment> filterPastOut(List<Appointment> inputList, String formattedDate, String formattedTime) {
        return inputList.stream()
                .filter(appointment -> appointment.getDate().compareTo(formattedDate) > 0 ||
                        appointment.getTime().compareTo(formattedTime) > 0)
                .collect(Collectors.toList());
    }


    private boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String timeString) {
        try {
            LocalTime.parse(timeString, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isFutureDatetime(String dateA, String timeA, String dateB, String timeB) {
        // Checks if Date-TimeA is in future of Date-TimeB
        return dateA.compareTo(dateB) > 0 || (dateA.equals(dateB) && timeA.compareTo(timeB) > 0);
    }


    private enum AvailabilityStatus {
        DOCTOR_BOOKED,
        PATIENT_BOOKED,
        AVAILABLE
    }

    private AvailabilityStatus availabilityCheck(Long doctorId, Long patientId, String date, String time) {
        boolean doctorAvailable = appointmentRepository
                .findByDoctorIdAndDateAndTime(doctorId, date, time).isEmpty();
        boolean patientAvailable = appointmentRepository
                .findByPatientIdAndDateAndTime(patientId, date, time).isEmpty();
        if (!doctorAvailable) {
            return AvailabilityStatus.DOCTOR_BOOKED;
        } else if (!patientAvailable) {
            return AvailabilityStatus.PATIENT_BOOKED;
        }
        return AvailabilityStatus.AVAILABLE;
    }
}
