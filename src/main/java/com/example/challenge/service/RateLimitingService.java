package com.example.challenge.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimitingService {

    private final StringRedisTemplate redisTemplate;
    private static final int LIMIT = 3;
    private static final Duration TIME_WINDOW = Duration.ofMinutes(1);

    public RateLimitingService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientIp) {
        String key = "rate_limit:" + clientIp;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, TIME_WINDOW);
        }

        return count <= LIMIT;
    }
}