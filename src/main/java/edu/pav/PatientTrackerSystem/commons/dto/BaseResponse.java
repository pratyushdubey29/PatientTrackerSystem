package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Represents a generic response structure containing HTTP status, a message, and a body of type T.
 *
 * @param <T> Type of the response body.
 */
@Data
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    /**
     * HTTP status of the response.
     */
    HttpStatus status;
    
    /**
     * A message associated with the response.
     */
    String msg;

    /**
     * The body of the response, of generic type T.
     */
    T body;
}
