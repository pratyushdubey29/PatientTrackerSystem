package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.NewAppointmentRequest;
import edu.pav.PatientTrackerSystem.commons.dto.RescheduleAppointmentRequest;
import edu.pav.PatientTrackerSystem.commons.dto.UserTypeAndIDRequest;
import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import edu.pav.PatientTrackerSystem.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.pav.PatientTrackerSystem.commons.Utils.*;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    CaseRepository caseRepository;

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
    public BaseResponse getTodaysAppointments(@RequestBody UserTypeAndIDRequest request) {

        String formattedCurrDate = getCurrentFormattedDate();

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
    public BaseResponse getFutureAppointments(@RequestBody UserTypeAndIDRequest request) {

        String formattedCurrDate = getCurrentFormattedDate();
        String formattedCurrTime = getCurrentFormattedTime();

        String userType = request.getUserType();
        if (!Objects.equals(userType.toLowerCase(), Constants.DOCTOR) &&
                !Objects.equals(userType.toLowerCase(), Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_USER_TYPE_STRING, Appointment.builder().build());
        }

        List<Appointment> retrievedAppointments;
        if (Objects.equals(request.getUserType().toLowerCase(), Constants.DOCTOR)) {
            // TODO: Check if User with that Id exists or not; Discuss if required
            retrievedAppointments = filterPastOut(appointmentRepository.findByDoctorId(request.getId()),
                    formattedCurrDate, formattedCurrTime);
        } else {
            // TODO: Check if User with that Id exists or not; Discuss if required
            retrievedAppointments = filterPastOut(appointmentRepository.findByPatientId(request.getId()),
                    formattedCurrDate, formattedCurrTime);
        }

        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENTS_FUTURE_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }

    @PostMapping(value = "appointments/create-in-open-case")
    public BaseResponse newAppointmentInOpenCase(@RequestBody NewAppointmentRequest request){
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

        Appointment newAppointment = Appointment.builder()
                .caseId(request.getCaseId()).patientId(request.getPatientId())
                .doctorId(request.getDoctorId()).date(requestedDate).time(requestedTime)
                .build();

        BaseResponse response = bookAppointment(newAppointment);
        return response;
    }


    @PostMapping(value = "appointments/reschedule")
    public BaseResponse rescheduleAppointments(@RequestBody RescheduleAppointmentRequest request) {

        String newTime = request.getNewTime();
        String newDate = request.getNewDate();

        DateTimeFormatStatus newDateTimeStatus = dateTimeFormatAndPastCheck(newDate, newTime);
        if (newDateTimeStatus == DateTimeFormatStatus.ILL_FORMATTED){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        } else if (newDateTimeStatus == DateTimeFormatStatus.PAST_DATETIME){
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
        }

        Long appointmentId = request.getAppointmentId();
        Optional<Appointment> currentAppointmentResponse = appointmentRepository.findById(appointmentId);

        // If appointment does not exist
        if (currentAppointmentResponse.isEmpty()) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND,
                    Constants.NO_APPOINTMENT_FOUND_STRING, Appointment.builder().build());
        }
        Appointment currentAppointment = currentAppointmentResponse.get();

        // If the appointment is already completed in the past
        if(dateTimeFormatAndPastCheck(currentAppointment.getDate(), currentAppointment.getTime())
                == DateTimeFormatStatus.PAST_DATETIME){
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.CANNOT_EDIT_PAST_APPOINTMENT, Appointment.builder().build());
        }

        Appointment updatedAppointment = Appointment.builder()
                .appointmentId(appointmentId).caseId(currentAppointment.getCaseId())
                .patientId(currentAppointment.getPatientId()).doctorId(currentAppointment.getDoctorId())
                .date(newDate).time(newTime)
                .build();
        return bookAppointment(updatedAppointment);
    }

//    @DeleteMapping(value = "/appointments/{id}")
    private void deleteAppointment(@PathVariable("id") Long id) {
        if (id != null) {
            appointmentRepository.deleteById(id);
        }
    }


    private List<Appointment> filterPastOut(List<Appointment> inputList, String formattedDate, String formattedTime) {
        return inputList.stream()
                .filter(appointment -> appointment.getDate().compareTo(formattedDate) > 0
                        || (appointment.getDate().equals(formattedDate) && appointment.getTime().compareTo(formattedTime) > 0))
                .collect(Collectors.toList());
    }

    public BaseResponse bookAppointment(Appointment appointment){
        AvailabilityStatus availabilityStatus = availabilityCheck(appointment.getDoctorId(),
                appointment.getPatientId(), appointment.getDate(), appointment.getTime());

        if (availabilityStatus == AvailabilityStatus.DOCTOR_BOOKED) {

            // If the doctor is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, Appointment.builder().build());
        } else if (availabilityStatus == AvailabilityStatus.PATIENT_BOOKED) {

            // If the patient is booked at the requested time
            return new BaseResponse<>(HttpStatus.OK,
                    Constants.SLOT_ALREADY_BOOKED_STRING + Constants.PATIENT, Appointment.builder().build());
        } else {
            // Book the appointment
            appointmentRepository.save(appointment);
            return new BaseResponse<>(HttpStatus.OK, Constants.SCHEDULE_APPOINTMENT_SUCCESSFUL, appointment);
        }
    }

    public AvailabilityStatus availabilityCheck(Long doctorId, Long patientId, String date, String time) {

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

    public enum AvailabilityStatus {
        DOCTOR_BOOKED,
        PATIENT_BOOKED,
        AVAILABLE
    }

}
