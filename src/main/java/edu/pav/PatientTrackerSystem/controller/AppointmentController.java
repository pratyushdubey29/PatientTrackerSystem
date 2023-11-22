package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
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
    public BaseResponse getTodaysAppointments(@RequestBody FetchAppointmentRequest request){

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
                    request.getId(), formattedCurrDate), formattedCurrDate ,formattedCurrTime);
        }

        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENTS_FUTURE_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }


    @PostMapping(value = "appointments/reschedule")
    public BaseResponse getRescheduleFutureAppointments(@RequestBody RescheduleAppointmentRequest request) {
        String newTime = request.getNewTime();
        String newDate = request.getNewDate();
        Long appointmentId = request.getAppointmentId();
        if (!isDateValid(newDate) || !isTimeValid(newTime)){
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_DATE_TIME_STRING, Appointment.builder().build());
        }
        Optional<Appointment> currentAppointment = appointmentRepository.findById(appointmentId);
        if (currentAppointment.isEmpty()){
            return new BaseResponse<>(HttpStatus.NOT_FOUND,
                    Constants.NO_APPOINTMENT_FOUND_STRING, Appointment.builder().build());
        }
        else {
            Long caseId = currentAppointment.get().getCaseId();
            Long patientId = currentAppointment.get().getPatientId();
            Long doctorId = currentAppointment.get().getDoctorId();
            String currAppointmentDate = currentAppointment.get().getDate();
            String currAppointmentTime = currentAppointment.get().getTime();

            String formattedCurrDate = LocalDate.now().format(dateFormatter);
            String formattedCurrTime = LocalTime.now().format(timeFormatter);

            if (formattedCurrDate.compareTo(currAppointmentDate) > 0 || (formattedCurrDate.equals(currAppointmentDate))
                    && formattedCurrTime.compareTo(currAppointmentTime) > 0){
                return new BaseResponse<>(HttpStatus.OK,
                        Constants.CANNOT_EDIT_PAST_APPOINTMENT, Appointment.builder().build());
            }
            if (newDate.compareTo(formattedCurrDate) > 0 || (newDate.equals(formattedCurrDate))
                    && newTime.compareTo(formattedCurrTime) > 0){

                boolean doctorAvailability = appointmentRepository
                        .findByDoctorIdAndDateAndTime(doctorId, newDate, newTime).isEmpty();
                boolean patientAvailability = appointmentRepository
                        .findByPatientIdAndDateAndTime(patientId, newDate, newTime).isEmpty();
                if (!doctorAvailability){
                    return new BaseResponse<>(HttpStatus.OK,
                            Constants.SLOT_ALREADY_BOOKED_STRING + Constants.DOCTOR, Appointment.builder().build());
                }else if (!patientAvailability){
                    return new BaseResponse<>(HttpStatus.OK,
                            Constants.SLOT_ALREADY_BOOKED_STRING + Constants.PATIENT, Appointment.builder().build());
                }else {
                    Appointment newAppointment  = Appointment.builder()
                            .appointmentId(appointmentId)
                            .caseId(caseId)
                            .patientId(patientId)
                            .doctorId(doctorId)
                            .date(newDate)
                            .time(newTime)
                            .build();
                    appointmentRepository.save(newAppointment);
                    return new BaseResponse<>(HttpStatus.OK, Constants.RESCHEDULE_SUCCESSFUL, newAppointment);
                }
            }
            else {
                return new BaseResponse<>(HttpStatus.OK,
                        Constants.PAST_DATE_TIME_STRING, Appointment.builder().build());
            }
        }
    }


    private List<Appointment> filterPastOut(List<Appointment> inputList, String formattedDate, String formattedTime){
        return inputList.stream()
                .filter(appointment -> appointment.getDate().compareTo(formattedDate) > 0 ||
                        appointment.getTime().compareTo(formattedTime) > 0)
                .collect(Collectors.toList());
    }


    private boolean isDateValid(String dateString) {
        try {
            LocalDate.parse(dateString, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isTimeValid(String timeString) {
        try {
            LocalTime.parse(timeString, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
