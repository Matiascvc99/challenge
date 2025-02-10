package com.example.challenge.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.challenge.api.model.ErrorMessage;
import com.example.challenge.api.model.Operation;
import com.example.challenge.api.model.Request;
import com.example.challenge.api.model.Response;
import com.example.challenge.service.FactorService;
import com.example.challenge.service.FetchOperationsService;
import com.example.challenge.service.RegisterOperationService;
import com.example.challenge.service.Validations;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class OperationController {
    @Autowired
    RegisterOperationService registerOperationService;
    @Autowired
    FactorService factorService;
    @Autowired
    FetchOperationsService fetchOperationsService;


    @PostMapping("/execute-operation")
    public ResponseEntity getUser(@RequestBody String requestBody) throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        Request request = objectMapper.readValue(requestBody, Request.class);
        
        
        Response response = new Response();
        String responseMessage = null;

        try {  
            Validations.validateRequest(request);
            Float factor = (float) factorService.factorConsumer()/100;
            Integer n1 = request.getRequestNum1();
            Integer n2 = request.getRequestNum2();
            Integer addition = n1 + n2;
            Float result = addition * (1 + factor);
            response.setResult(result);
            response.setFactor(factor);
            responseMessage = objectMapper.writeValueAsString(response);
            return new ResponseEntity(responseMessage, HttpStatus.OK);

        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("Bad Request");
            errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
            errorMessage.setLevel("Error");
            errorMessage.setDescription(e.getMessage());
            responseMessage = objectMapper.writeValueAsString(errorMessage);
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);

        } finally {

            Operation operation = new Operation();
            operation.setOperationDate(java.time.LocalDateTime.now());
            operation.setOperationEndpoint("/execute-operation");
            operation.setOperationResponse(responseMessage);
            operation.setOperationRequest(requestBody);

            registerOperationService.saveOperation(operation);
        }
        
    };

    @GetMapping("/fetch-operations")
    public ResponseEntity<Page<Operation>> fetchOperations(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) throws JsonProcessingException 
        
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Response response = new Response();
        String responseMessage = null;
        try{
            Page<Operation> operations = fetchOperationsService.getOperations(page, size);
            responseMessage = operations.getContent().toString();
            return new ResponseEntity<>(operations, HttpStatus.OK);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("Bad Request");
            errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
            errorMessage.setLevel("Error");
            errorMessage.setDescription(e.getMessage());
            responseMessage = objectMapper.writeValueAsString(errorMessage);
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } finally {
            Operation operation = new Operation();
            operation.setOperationDate(java.time.LocalDateTime.now());
            operation.setOperationEndpoint("/fetch-operations");
            operation.setOperationResponse(responseMessage);
            operation.setOperationRequest("Page: " + page + ", Size: " + size);

            registerOperationService.saveOperation(operation);
        }
        
    }
}
