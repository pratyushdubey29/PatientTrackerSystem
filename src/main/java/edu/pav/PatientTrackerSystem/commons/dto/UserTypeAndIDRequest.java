package edu.pav.PatientTrackerSystem.commons.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class UserTypeAndIDRequest {

    @NonNull
    private String userType;
    @NonNull
    private Long id;

}