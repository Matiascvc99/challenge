package com.example.challenge.api.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.challenge.api.model.Operation;

@Repository
public interface FetchDataRepository extends JpaRepository<Operation, Integer> {
    Page<Operation> findAll(Pageable pageable);
}