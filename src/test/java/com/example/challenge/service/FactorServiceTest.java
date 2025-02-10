package com.example.challenge.service;

import com.example.challenge.service.FactorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactorServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private FactorApiCall factorApiCall;

    @InjectMocks
    private FactorService factorService;

    private static final String FACTOR_KEY = "cachedFactor";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldReturnFactorFromApiAndCacheIt() {
        Integer factor = factorApiCall.factorAPICall();
        when(factor).thenReturn(42);

        Integer result = factorService.factorConsumer();

        assertEquals(42, result);
        verify(valueOperations).set(eq(FACTOR_KEY), eq("42"), eq(Duration.ofMinutes(30)));
    }

    @Test
    void shouldReturnCachedFactorWhenApiFails() {
        when(factorApiCall.factorAPICall()).thenThrow(new RuntimeException("API response was null or empty"));
        when(valueOperations.get(FACTOR_KEY)).thenReturn("99");

        Integer result = factorService.factorConsumer();

        assertEquals(99, result);
    }

    @Test
    void shouldThrowExceptionWhenApiFailsAndNoCacheExists() {
        when(factorApiCall.factorAPICall()).thenThrow(new RuntimeException("API response was null or empty"));
        when(valueOperations.get(FACTOR_KEY)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> factorService.factorConsumer());

        assertEquals("Unable to retrieve factor: API failed and no cached value available.", exception.getMessage());
    }
}