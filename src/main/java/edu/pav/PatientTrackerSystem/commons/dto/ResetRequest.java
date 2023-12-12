package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request object containing user type and email information.
 * This class is intended for use with request parameters.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class ResetRequest {

    /**
     * The user type associated with the request.
     */
    @NonNull
    private String userType;

    /**
     * The unique email associated with the user.
     */
    @NonNull
    private String email;

    /**
     * The OTP sent by the user.
     */
    @NonNull
    private String OTP;

    /**
     * The new password chosen by the patient for authentication.
     */
    @NonNull
    private String password;

}
