package com.vikas.ratelimiter.service;

public interface RateLimiter {
    boolean allowRequest(String key);
}