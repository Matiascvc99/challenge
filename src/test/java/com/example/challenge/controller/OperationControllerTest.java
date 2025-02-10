package com.example.challenge.controller;

import com.example.challenge.api.controller.OperationController;
import com.example.challenge.api.model.ErrorMessage;
import com.example.challenge.api.model.Operation;
import com.example.challenge.api.model.Request;
import com.example.challenge.api.model.Response;
import com.example.challenge.service.FactorService;
import com.example.challenge.service.FetchOperationsService;
import com.example.challenge.service.RegisterOperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationControllerTest {

    @Mock
    private RegisterOperationService registerOperationService;
    @Mock
    private FactorService factorService;
    @Mock
    private FetchOperationsService fetchOperationsService;

    @InjectMocks
    private OperationController operationController;

    @Test
    public void testExecuteOperationSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request();
        request.setRequestNum1(5);
        request.setRequestNum2(10);

        when(factorService.factorConsumer()).thenReturn(50); // Simulate a factor of 0.5

        ResponseEntity<?> responseEntity = operationController.getUser(objectMapper.writeValueAsString(request));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = objectMapper.readValue(responseEntity.getBody().toString(), Response.class);
        assertEquals(22.5f, response.getResult());
        assertEquals(0.5f, response.getFactor());

        verify(registerOperationService, times(1)).saveOperation(any(Operation.class));
    }



    @Test
    public void testExecuteOperationBadRequest() throws Exception {
        String requestBody = "{\"requestNum1\":-5,\"requestNum2\":10}"; // Invalid request

        ResponseEntity<?> responseEntity = operationController.getUser(requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(registerOperationService, times(1)).saveOperation(any(Operation.class));

    }

    @Test
    public void testExecuteOperationException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = new Request();
        request.setRequestNum1(5);
        request.setRequestNum2(10);

        when(factorService.factorConsumer()).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<?> responseEntity = operationController.getUser(objectMapper.writeValueAsString(request));


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        String responseMessage = responseEntity.getBody().toString();
        ErrorMessage errorMessage = objectMapper.readValue(responseMessage, ErrorMessage.class);

        verify(registerOperationService, times(1)).saveOperation(any(Operation.class));

    }

    @Test
    public void testFetchOperationsSuccess() throws Exception {
        int page = 0;
        int size = 10;
        List<Operation> operationList = new ArrayList<>();
        Page<Operation> operationsPage = new PageImpl<>(operationList);

        when(fetchOperationsService.getOperations(page, size)).thenReturn(operationsPage);

        ResponseEntity<Page<Operation>> responseEntity = operationController.fetchOperations(page, size);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(operationsPage, responseEntity.getBody());
        verify(registerOperationService, times(1)).saveOperation(any(Operation.class));

    }


    @Test
    void testFetchOperationsException() throws Exception {
        int page = 0;
        int size = 10;
        when(fetchOperationsService.getOperations(page, size)).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<?> responseEntity = operationController.fetchOperations(page, size);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(registerOperationService, times(1)).saveOperation(any(Operation.class));
    }




}

