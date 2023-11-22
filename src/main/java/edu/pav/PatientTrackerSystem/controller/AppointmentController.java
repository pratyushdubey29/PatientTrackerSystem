package edu.pav.PatientTrackerSystem.controller;

import edu.pav.PatientTrackerSystem.commons.Constants;
import edu.pav.PatientTrackerSystem.commons.dto.BaseResponse;
import edu.pav.PatientTrackerSystem.commons.dto.FetchAppointmentRequest;
import edu.pav.PatientTrackerSystem.model.Appointment;
import edu.pav.PatientTrackerSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        String formattedToday = LocalDate.now().format(dateFormatter);

        String userType = request.getUserType();
        if (!Objects.equals(userType.toLowerCase(), Constants.DOCTOR) &&
                !Objects.equals(userType.toLowerCase(), Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_USER_TYPE_STRING, Appointment.builder().build());
        }

        List<Appointment> retrievedAppointments;
        if (Objects.equals(request.getUserType().toLowerCase(), Constants.DOCTOR)) {
            retrievedAppointments = appointmentRepository.findByDoctorIdAndDate(request.getId(), formattedToday);
        } else {
            retrievedAppointments = appointmentRepository.findByPatientIdAndDate(request.getId(), formattedToday);
        }
        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENTS_TODAY_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }

    @PostMapping(value = "/appointments/view-future")
    public BaseResponse getFutureAppointments(@RequestBody FetchAppointmentRequest request) {

        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);

        String userType = request.getUserType();
        if (!Objects.equals(userType.toLowerCase(), Constants.DOCTOR) &&
                !Objects.equals(userType.toLowerCase(), Constants.PATIENT)) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_USER_TYPE_STRING, Appointment.builder().build());
        }

        List<Appointment> retrievedAppointments;
        if (Objects.equals(request.getUserType().toLowerCase(), Constants.DOCTOR)) {
            retrievedAppointments = filterPastOut(appointmentRepository.findByDoctorIdAndDateGreaterThanEqual(
                    request.getId(), formattedDate), formattedDate, formattedTime);
        } else {
            retrievedAppointments = filterPastOut(appointmentRepository.findByPatientIdAndDateGreaterThanEqual(
                    request.getId(), formattedDate), formattedDate ,formattedTime);
        }

        String message = retrievedAppointments.isEmpty() ? Constants.NO_APPOINTMENTS_FUTURE_STRING : Constants.SUCCESS;

        return new BaseResponse<>(HttpStatus.OK, message, retrievedAppointments);
    }





    private List<Appointment> filterPastOut(List<Appointment> inputList, String formattedDate, String formattedTime){
        return inputList.stream()
                .filter(appointment -> appointment.getDate().compareTo(formattedDate) > 0 ||
                        appointment.getTime().compareTo(formattedTime) > 0)
                .collect(Collectors.toList());
    }

}
