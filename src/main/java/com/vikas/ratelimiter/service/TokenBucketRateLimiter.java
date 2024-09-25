package com.vikas.ratelimiter.service;


import com.vikas.ratelimiter.model.PricingPlan;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final PricingPlan pricingPlan;

    public TokenBucketRateLimiter(PricingPlan pricingPlan) {
        this.pricingPlan = pricingPlan;
    }

    @Override
    public boolean allowRequest(String key) {
        TokenBucket bucket = buckets.computeIfAbsent(key, k -> new TokenBucket(pricingPlan.getLimit(), pricingPlan.getRefillRate()));
        return bucket.tryConsume();
    }
}