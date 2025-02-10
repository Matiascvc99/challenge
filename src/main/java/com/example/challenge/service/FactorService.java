package com.example.challenge.service;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;

import com.example.challenge.service.FactorApiCall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class FactorService {

    private static final String FACTOR_KEY = "cachedFactor";
    private static final int MAX_RETRIES = 3;

    @Autowired
    private FactorApiCall factorApiCall;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Integer factorConsumer() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                attempts++;
                Integer factor = factorApiCall.factorAPICall();
                ops.set(FACTOR_KEY, factor.toString(), Duration.ofMinutes(30));
                return factor;
                
            } catch (Exception e) {
                System.out.println("Attempt " + attempts + " failed, retrying...");
                if (attempts == MAX_RETRIES){
                    System.err.println("Failed to fetch from API, trying cache... " + e.getMessage());
                }
                
            }
        }
        String cachedValue = ops.get(FACTOR_KEY);
        if (cachedValue != null) {
            return Integer.parseInt(cachedValue);
        }

        throw new RuntimeException("Unable to retrieve factor: API failed and no cached value available.");
    }
}
