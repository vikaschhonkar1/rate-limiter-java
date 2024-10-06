package com.vikas.ratelimiter.aspect;

import com.vikas.ratelimiter.annotation.RateLimit;
import com.vikas.ratelimiter.model.PricingPlan;
import com.vikas.ratelimiter.service.RateLimiter;
import com.vikas.ratelimiter.service.RateLimiterFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RateLimitAspect {

    @Autowired
    private RateLimiterFactory rateLimiterFactory;

    @Around("@annotation(rateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String userId = null;
        for (Object arg : args) {
            if (arg instanceof String && arg.toString().contains("user")) {
                userId = (String) arg;
                break;
            }
        }

        PricingPlan pricingPlan;
        try {
            pricingPlan = PricingPlan.valueOf(rateLimit.pricingPlan().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid pricing plan");
        }

        RateLimiter rateLimiter = rateLimiterFactory.getRateLimiter(pricingPlan);

        if (rateLimiter.allowRequest(userId)) {
            return joinPoint.proceed();
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}

