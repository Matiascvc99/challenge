package com.example.challenge.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Generated;

@Generated
public class Request {
    @JsonAlias("num1")
    private Integer requestNum1;
    @JsonAlias("num2")
    private Integer requestNum2;


    public Integer getRequestNum1() {
        return requestNum1;
    }
    public void setRequestNum1(Integer requestNum1) {
        this.requestNum1 = requestNum1;
    }
    public Integer getRequestNum2() {
        return requestNum2;
    }
    public void setRequestNum2(Integer requestNum2) {
        this.requestNum2 = requestNum2;
    }
}
