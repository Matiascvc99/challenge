package com.example.challenge.api.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.challenge.api.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {
}
