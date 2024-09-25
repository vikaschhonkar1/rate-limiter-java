package com.vikas.ratelimiter.service;

import com.vikas.ratelimiter.model.PricingPlan;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterFactory {
    private final ConcurrentHashMap<PricingPlan, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    public RateLimiter getRateLimiter(PricingPlan pricingPlan) {
        return rateLimiters.computeIfAbsent(pricingPlan, TokenBucketRateLimiter::new);
    }
}
