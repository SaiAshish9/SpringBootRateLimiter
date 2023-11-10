package com.sai.rateLimiter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    public ResponseEntity<String> invokeRateLimiterTest() {
        return ResponseEntity.ok("Rate Limiter Test");
    }

}
