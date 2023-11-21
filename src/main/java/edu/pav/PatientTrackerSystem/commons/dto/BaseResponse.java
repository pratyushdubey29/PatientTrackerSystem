package edu.pav.PatientTrackerSystem.commons.dto;

import org.springframework.http.HttpStatus;

public class BaseResponse<T> {

    HttpStatus status;
    String errorMsg;
    T body;
}
