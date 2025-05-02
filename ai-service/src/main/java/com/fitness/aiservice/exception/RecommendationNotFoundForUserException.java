package com.fitness.aiservice.exception;

public class RecommendationNotFoundForUserException extends RuntimeException {
    public RecommendationNotFoundForUserException(String message) {
        super(message);
    }
}
