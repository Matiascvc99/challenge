package com.example.challenge.service;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Generated;

@Generated
@Service
public class FactorApiCall {
    private static final String FACTOR_URL = "http://www.randomnumberapi.com/api/v1.0/random";

    public Integer factorAPICall() {

        RestTemplate template = new RestTemplate();

        ResponseEntity<List<Integer>> factorList = template.exchange(
            FACTOR_URL,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<List<Integer>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            }
        );
        if (factorList.getBody() != null && !factorList.getBody().isEmpty()){
            return factorList.getBody().get(0);
        } else {
            throw new RuntimeException("API response was null or empty");
        }
        
    }
}
