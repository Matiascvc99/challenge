package com.example.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.challenge.api.model.Operation;
import com.example.challenge.api.repos.OperationRepository;

@Service
public class RegisterOperationService {
    @Autowired
    private OperationRepository operationRepository;
    @Transactional
    public Operation saveOperation(Operation operation) {
        return operationRepository.save(operation);
    }
}
