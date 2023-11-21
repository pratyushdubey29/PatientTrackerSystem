package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class DoctorSignupRequest {

    @JsonProperty(value = "user_name")
    private String userName;

    private String password;

    private String dob;

    private String name;

    private String hospital;

    private String speciality;

    private String address;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String email;
}
