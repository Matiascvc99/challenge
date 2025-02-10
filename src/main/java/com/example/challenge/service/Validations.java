package com.example.challenge.service;

import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import com.example.challenge.api.model.Request;

public class Validations {
    
    public static void validateRequest(Request request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request must not be null");
        }
        if (request.getRequestNum1() == null || request.getRequestNum2() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request parameters must not be null");
        }
        if (request.getRequestNum1() < 0 || request.getRequestNum2() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request parameters must not be negative");
        }

    }
    
}
