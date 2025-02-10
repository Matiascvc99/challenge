package com.example.challenge.config;

import com.example.challenge.service.RateLimitingService;
import com.example.challenge.api.config.RateLimitFilter;
import com.example.challenge.api.model.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitingService rateLimitingService;
    private RateLimitFilter rateLimitFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        rateLimitingService = mock(RateLimitingService.class);
        rateLimitFilter = new RateLimitFilter(rateLimitingService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void shouldProceedWhenRequestIsAllowed() throws IOException, ServletException {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimitingService.isAllowed("127.0.0.1")).thenReturn(true);

        rateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @Test
    void shouldReturn429WhenRequestIsNotAllowed() throws IOException, ServletException {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimitingService.isAllowed("127.0.0.1")).thenReturn(false);

        // Capture the response body
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        rateLimitFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        writer.flush();
        String jsonResponse = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMessage errorMessage = objectMapper.readValue(jsonResponse, ErrorMessage.class);

        assertEquals("Too Many Requests", errorMessage.getMessage());
        assertEquals(429, errorMessage.getCode());
        assertEquals("Error", errorMessage.getLevel());
        assertEquals("You're doing that too often! Try again later.", errorMessage.getDescription());
    }
}