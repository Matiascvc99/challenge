package com.example.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.example.challenge.api.model.Request;

class ValidationsTest {

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Validations.validateRequest(null);
        });
        assertEquals("400 BAD_REQUEST \"Request must not be null\"", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenParametersAreNull() {
        Request request = new Request();
        request.setRequestNum1(null);
        request.setRequestNum2(null);
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Validations.validateRequest(request);
        });
        assertEquals("400 BAD_REQUEST \"Request parameters must not be null\"", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenParametersAreNegative() {
        Request request = new Request();
        request.setRequestNum1(-1);
        request.setRequestNum2(5);
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Validations.validateRequest(request);
        });
        assertEquals("400 BAD_REQUEST \"Request parameters must not be negative\"", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenRequestIsValid() {
        Request request = new Request();
        request.setRequestNum1(10);
        request.setRequestNum2(20);
        
        assertDoesNotThrow(() -> Validations.validateRequest(request));
    }
}
