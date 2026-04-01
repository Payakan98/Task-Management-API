package com.example.taskshift.security;

public class RateLimitException extends RuntimeException {
    
    public RateLimitException(String message) {
        super(message);
    }
}