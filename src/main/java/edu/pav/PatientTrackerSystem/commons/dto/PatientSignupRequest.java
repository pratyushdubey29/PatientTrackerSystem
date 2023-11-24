package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class PatientSignupRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String dob;
    @NonNull
    private String name;
    @NonNull
    private char sex;
    @NonNull
    private int weight;
    @NonNull
    private int height;
    @NonNull
    private String address;
    @NonNull
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
}
