package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class LoginDto {

    @NonNull
    private String userName;

    @NonNull
    private String password;
}
