package com.example.challenge.api.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.challenge.api.model.ErrorMessage;
import com.example.challenge.service.RateLimitingService;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class RateLimitFilter implements Filter {

    private final RateLimitingService rateLimitingService;

    public RateLimitFilter(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr();

        if (!rateLimitingService.isAllowed(clientIp)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            ObjectMapper objectMapper = new ObjectMapper();

            httpResponse.setStatus(429);
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("Too Many Requests");
            errorMessage.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
            errorMessage.setLevel("Error");
            errorMessage.setDescription("You're doing that too often! Try again later.");
            String responseMessage = objectMapper.writeValueAsString(errorMessage);
            httpResponse.getWriter().write(responseMessage);
            return;
        }

        chain.doFilter(request, response);
    }

}