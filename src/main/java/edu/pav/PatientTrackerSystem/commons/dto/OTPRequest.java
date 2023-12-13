package edu.pav.PatientTrackerSystem.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request object containing user type and email information.
 * This class is intended for use with request parameters.
 */
@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class OTPRequest {
    /**
     * The unique email associated with the user.
     */
    @NonNull
    private String appendedEmail;

}
