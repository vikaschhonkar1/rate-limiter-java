package com.vikas.ratelimiter.model;

public enum PricingPlan {
    FREE(10, 1),
    BASIC(20, 2),
    PROFESSIONAL(50, 5),
    ENTERPRISE(100, 10);

    private final int limit;
    private final int refillRate;

    PricingPlan(int limit, int refillRate) {
        this.limit = limit;
        this.refillRate = refillRate;
    }

    public int getLimit() {
        return limit;
    }

    public int getRefillRate() {
        return refillRate;
    }
}