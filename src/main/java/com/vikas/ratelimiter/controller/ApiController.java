package com.vikas.ratelimiter.controller;

import com.vikas.ratelimiter.model.PricingPlan;
import com.vikas.ratelimiter.service.RateLimiter;
import com.vikas.ratelimiter.service.RateLimiterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {
    @Autowired
    private RateLimiterFactory rateLimiterFactory;
    

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("User-ID") String userId,
                                       @RequestHeader("Pricing-Plan") String pricingPlanStr) {
        PricingPlan pricingPlan;
        try {
            pricingPlan = PricingPlan.valueOf(pricingPlanStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid pricing plan");
        }

        RateLimiter rateLimiter = rateLimiterFactory.getRateLimiter(pricingPlan);

        if (rateLimiter.allowRequest(userId)) {
            return ResponseEntity.ok("Request successful");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}