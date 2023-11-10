package com.sai.rateLimiter.controller;

import com.sai.rateLimiter.service.RateLimiterService;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @PostConstruct
    public void postConstruct() {
        io.github.resilience4j.ratelimiter.RateLimiter.EventPublisher eventPublisher = rateLimiterRegistry
                .rateLimiter("basic")
                .getEventPublisher();

        eventPublisher.onSuccess(System.out::println);
        eventPublisher.onFailure(System.out::println);
    }

    @PostMapping("/test")
    @RateLimiter(name = "basic", fallbackMethod = "triggerFallbackMethod")
    public ResponseEntity testRateLimiter() {
        io.github.resilience4j.ratelimiter.RateLimiter limiter = rateLimiterRegistry.rateLimiter("basic");
//        limiter.changeLimitForPeriod(100);
        return rateLimiterService.invokeRateLimiterTest();
    }

    public ResponseEntity<String> triggerFallbackMethod(RequestNotPermitted requestNotPermitted) {
        return new ResponseEntity<String>(requestNotPermitted.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

}
