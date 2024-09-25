package com.vikas.ratelimiter.service;


import java.time.Instant;

public class TokenBucket {
    private final int capacity;
    private final int refillRate;
    private int tokens;
    private long lastRefillTimestamp;

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = Instant.now().getEpochSecond();
    }

    public synchronized boolean tryConsume() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = Instant.now().getEpochSecond();
        long timePassed = now - lastRefillTimestamp;
        int tokensToAdd = (int) (timePassed * refillRate);
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}
