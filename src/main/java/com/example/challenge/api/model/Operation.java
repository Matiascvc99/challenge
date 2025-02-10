package com.example.challenge.api.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
@Entity
@Table(name = "Operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int operationId;
    private LocalDateTime operationDate;
    private String operationEndpoint;
    private String operationResponse;
    private String operationRequest;


    public int getOperationId() {
        return operationId;
    }
    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }
    public LocalDateTime getOperationDate() {
        return operationDate;
    }
    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }
    public String getOperationEndpoint() {
        return operationEndpoint;
    }
    public void setOperationEndpoint(String operationEndpoint) {
        this.operationEndpoint = operationEndpoint;
    }
    public String getOperationResponse() {
        return operationResponse;
    }
    public void setOperationResponse(String operationResponse) {
        this.operationResponse = operationResponse;
    }
    public String getOperationRequest() {
        return operationRequest;
    }
    public void setOperationRequest(String operationRequest) {
        this.operationRequest = operationRequest;
    }
}
