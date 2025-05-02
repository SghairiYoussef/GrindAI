package com.fitness.aiservice.exception;

public class RecommendationNotFoundForActivityException extends RuntimeException {
    public RecommendationNotFoundForActivityException(String message) {
        super(message);
    }
}
