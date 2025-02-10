package com.example.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.challenge.api.model.Operation;
import com.example.challenge.api.repos.FetchDataRepository;


@Service
public class FetchOperationsService {

    @Autowired
    private FetchDataRepository operationRepository;

    public Page<Operation> getOperations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return operationRepository.findAll(pageable);
    }
}