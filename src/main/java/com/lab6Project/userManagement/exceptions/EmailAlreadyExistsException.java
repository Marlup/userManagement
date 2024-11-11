package com.lab6Project.userManagement.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);  // Pass the message to the parent class (RuntimeException)
    }
}