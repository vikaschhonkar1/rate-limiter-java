package com.vikas.ratelimiter.controller;

import com.vikas.ratelimiter.annotation.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @RateLimit(pricingPlan = "BASIC")
    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("User-ID") String userId) {
        return ResponseEntity.ok("Request successful");
    }
    
    @RateLimit(pricingPlan = "ENTERPRISE")
    @GetMapping("/test2")
    public ResponseEntity<String> test2(@RequestHeader("User-ID") String userId) {
        return ResponseEntity.ok("Request successful from test 2");
    }
}
