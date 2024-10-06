

# Rate Limiter API Implementation

## Overview

This project demonstrates the implementation of a **Rate Limiter** for a REST API as per different pricing plans. The purpose of the rate limiter is to restrict the number of requests an API client can make within a specified time window to prevent abuse or overuse. The system is designed using the **Token Bucket Algorithm**. This README provides a complete guide on the design, implementation, and decisions made throughout the development.

---

## Table of Contents

- [Project Setup](#project-setup)
- [API Endpoints](#api-endpoints)
- [Rate Limiter Design](#rate-limiter-design)
- [High-Level Design (HLD)](#high-level-design-hld)
- [Assumptions Made](#assumptions-made)
- [Algorithm Choice: Why Token Bucket?](#algorithm-choice-why-token-bucket)
- [Handling Edge Cases](#handling-edge-cases)
- [Future Improvements](#future-improvements)

---

## Project Setup

### Prerequisites

- Java 11+
- Maven
- Postman or curl for testing

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/vikaschhonkar1/rate-limiter-java.git
    ```

2. Build the project using Maven:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

4. Access the API with the headers - User-ID and Pricing-Plan:
    ```
    http://localhost:8086/test
    ```

---

## API Endpoints

- `GET /test`: A simple endpoint that returns an OK response if provided with the headers User-ID and Pricing-Plan.
  
- **Rate-Limited**: The API limits requests based on the rate-limiting rules defined using the Token Bucket algorithm. If the limit is exceeded, a `429 Too Many Requests` response is returned.

---

## Rate Limiter Design

### Key Concepts

1. **Token Bucket Algorithm**: This algorithm controls the rate of requests by maintaining a bucket of tokens. Each incoming request consumes a token, and the tokens are replenished at a fixed rate. If no tokens are available, the request is denied.

2. **Pricing Plan**: The system supports multiple pricing plans, where different clients may have different limits and refill rates.

---

## High-Level Design (HLD)

### Components

1. **REST API**: Provides a simple endpoint `/test` to demonstrate rate limiting in action.

2. **Rate Limiter**: Responsible for managing tokens and enforcing rate limits.

3. **Pricing Plan**: Defines the number of requests allowed per minute and how quickly tokens are replenished.

4. **Token Bucket**: Holds the current token count and logic for replenishing and consuming tokens.

5. **Rate Limiter Factory**: Produces rate limiters based on the user’s pricing plan.

### Flow

1. A client sends a request to the API.
2. The API calls the rate limiter to check if the request can be processed.
3. The rate limiter checks if there are tokens in the bucket.
4. If tokens are available, the request is processed, and a token is consumed.
5. If no tokens are available, a `429 Too Many Requests` response is returned.

---

## Assumptions Made

1. The rate limiter will operate server-side to ensure that incoming requests are throttled based on predefined limits.
2. The rate limiter will be integrated into the API Gateway, typically used to manage and route traffic for most applications.
3. The current implementation is not designed for distributed systems. However, it can be adapted for distributed environments using external state management systems such as Redis.
4. When requests are throttled, the system will return clear and descriptive exceptions to the client, indicating that the rate limit has been exceeded.
5. The pricing plans are currently managed in the form of enums:

   ```java
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
   ```

---

## Algorithm Choice: Why Token Bucket?

The Token Bucket algorithm is our chosen approach for this implementation due to its flexibility and efficiency.

### Advantages

- Allows for burst traffic up to the bucket capacity.
- Constant time complexity for both allowing requests and refilling tokens.
- Easy to implement and understand.

### Comparison with Other Algorithms

- **Leaky Bucket**
  - **Pros**: Smooths out bursts of traffic.
  - **Cons**: Doesn't allow for any bursts, which can be less user-friendly.
  - **Why not chosen**: Less flexible than Token Bucket for handling varying traffic patterns.

- **Fixed Window Counter**
  - **Pros**: Simple to implement.
  - **Cons**: Can lead to traffic spikes at window boundaries.
  - **Why not chosen**: Less accurate and can allow double the rate limit at window edges.

- **Sliding Window Log**
  - **Pros**: Very accurate.
  - **Cons**: Memory-intensive, especially for high-volume traffic.
  - **Why not chosen**: Higher memory usage and computational complexity compared to Token Bucket.

- **Sliding Window Counter**
  - **Pros**: Good balance between accuracy and performance.
  - **Cons**: More complex than Token Bucket.
  - **Why not chosen**: Token Bucket provides similar benefits with simpler implementation.

---

## Handling Edge Cases

1. **Different Users/Pricing Plans**: Each user is assigned a rate limiter based on their pricing plan, ensuring personalized rate limits.
  
2. **Rate Limit Reached**: If the bucket is empty, the system returns an HTTP 429 response with a message indicating too many requests.

---

## Future Improvements

Currently, the rate limiter is in-memory. To ensure rate limits persist across restarts, the following changes can be made:

- Use Redis or another in-memory data store to maintain state.
- Store token buckets' state periodically in a database.

---

## References

- [System Design: Design a Rate Limiter](https://medium.com/geekculture/system-design-design-a-rate-limiter-81d200c9d392) - An article discussing the principles of designing a rate limiter.
- [System Design: Rate Limiter and Data Modelling](https://medium.com/@saisandeepmopuri/system-design-rate-limiter-and-data-modelling-9304b0d18250) - A comprehensive guide on rate limiting and data modeling.
- [YouTube: Rate Limiter System Design](https://www.youtube.com/watch?v=9CIjoWPwAhU) - A video explaining the concepts and implementation of a rate limiter.

---
