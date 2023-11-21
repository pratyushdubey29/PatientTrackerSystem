package edu.pav.PatientTrackerSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    HttpStatus status;
    String errorMsg;
    T body;
}
