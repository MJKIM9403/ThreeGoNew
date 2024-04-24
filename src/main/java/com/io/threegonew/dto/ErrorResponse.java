package com.io.threegonew.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private String errorCode;
    private String errorMsg;

    public ErrorResponse(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String errorCode, String errorMsg) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("errorCode", errorCode);
        errorAttributes.put("errorMsg", errorMsg);

        return new ResponseEntity<>(errorAttributes, status);
    }
}
