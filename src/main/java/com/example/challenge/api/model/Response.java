package com.example.challenge.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Generated;

@Generated
public class Response {
    @JsonAlias("result")
    private Float result;
    @JsonAlias("factor")
    private Float factor;
    public Float getResult() {
        return result;
    }
    public void setResult(Float result) {
        this.result = result;
    }
    public Float getFactor() {
        return factor;
    }
    public void setFactor(Float factor) {
        this.factor = factor;
    }

}
