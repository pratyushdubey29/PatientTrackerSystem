package edu.pav.PatientTrackerSystem.commons.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a request object containing user type and ID information.
 * This class is intended for use with request parameters.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class UserTypeAndIDRequest {
    // TODO: Deprecate to RequestParams

    /**
     * The user type associated with the request.
     */
    @NonNull
    private String userType;

    /**
     * The unique identifier associated with the user.
     */
    @NonNull
    private Long id;

}