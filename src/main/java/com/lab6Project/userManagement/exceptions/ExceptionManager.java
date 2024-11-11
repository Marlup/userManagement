package com.lab6Project.userManagement.exceptions;

import com.lab6Project.userManagement.dto.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice  // This makes it a global exception handler
public class ExceptionManager {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<JwtResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        // Create the response body with the exception's message
        JwtResponse response = JwtResponse.builder()
                .build();

        // Return a BAD_REQUEST response (HTTP 400) with the message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // You can add more exception handlers here for different exception types
}
