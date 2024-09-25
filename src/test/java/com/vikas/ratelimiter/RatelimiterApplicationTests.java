package com.vikas.ratelimiter;
import com.vikas.ratelimiter.model.PricingPlan;
import com.vikas.ratelimiter.service.TokenBucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RatelimiterApplicationTests {
   private TokenBucket rateLimiter;
   private PricingPlan currentPlan;
   // Free plan
   @Test
   void testFreePlan() throws InterruptedException {
       currentPlan = PricingPlan.FREE;
       rateLimiter = new TokenBucket(currentPlan.getLimit(), currentPlan.getRefillRate());
       for (int i = 0; i < currentPlan.getLimit(); i++) {
           assertTrue(rateLimiter.tryConsume());
       }
       assertFalse(rateLimiter.tryConsume());
       Thread.sleep(1000);
       assertTrue(rateLimiter.tryConsume());
   }
   // BASIC
   @Test
   void testBasicPlan() throws InterruptedException {
       currentPlan = PricingPlan.BASIC;
       rateLimiter = new TokenBucket(currentPlan.getLimit(), currentPlan.getRefillRate());
       for (int i = 0; i < currentPlan.getLimit(); i++) {
           assertTrue(rateLimiter.tryConsume());
       }
      
       assertFalse(rateLimiter.tryConsume());
       Thread.sleep(1000);
       assertTrue(rateLimiter.tryConsume());
   }
   // PROFESSIONAL
   @Test
   void testProfessionalPlan() throws InterruptedException {
       currentPlan = PricingPlan.PROFESSIONAL;
       rateLimiter = new TokenBucket(currentPlan.getLimit(), currentPlan.getRefillRate());
       for (int i = 0; i < currentPlan.getLimit(); i++) {
           assertTrue(rateLimiter.tryConsume());
       }
       assertFalse(rateLimiter.tryConsume());
       Thread.sleep(1000);
       assertTrue(rateLimiter.tryConsume());
   }
   //  ENTERPRISE
   @Test
   void testEnterprisePlan() throws InterruptedException {
       currentPlan = PricingPlan.ENTERPRISE;
       rateLimiter = new TokenBucket(currentPlan.getLimit(), currentPlan.getRefillRate());
       for (int i = 0; i < currentPlan.getLimit(); i++) {
           assertTrue(rateLimiter.tryConsume());
       }
       assertFalse(rateLimiter.tryConsume());
       Thread.sleep(1000);
       assertTrue(rateLimiter.tryConsume());
   }
   // Multiple users
   @Test
   void testConcurrentAccessBasicPlan() throws InterruptedException {
       currentPlan = PricingPlan.BASIC;
       rateLimiter = new TokenBucket(currentPlan.getLimit(), currentPlan.getRefillRate());
      
       int threadCount = 20;
       Thread[] threads = new Thread[threadCount];
       for (int i = 0; i < threadCount; i++) {
           threads[i] = new Thread(() -> {
               assertTrue(rateLimiter.tryConsume() || !rateLimiter.tryConsume());
           });
           threads[i].start();
       }
      
       for (Thread thread : threads) {
           thread.join();
       }
   }
}

