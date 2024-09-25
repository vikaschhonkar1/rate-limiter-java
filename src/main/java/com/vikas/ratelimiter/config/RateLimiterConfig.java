

package com.vikas.ratelimiter.config;

import com.vikas.ratelimiter.service.RateLimiterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiterFactory rateLimiterFactory() {
        return new RateLimiterFactory();
    }
}
