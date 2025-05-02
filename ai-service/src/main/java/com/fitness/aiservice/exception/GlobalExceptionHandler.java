package com.fitness.aiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecommendationNotFoundForActivityException.class)
    public ResponseEntity<String> handleRecommendationNotFoundForActivityException(RecommendationNotFoundForActivityException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(RecommendationNotFoundForUserException.class)
    public ResponseEntity<String> handleRecommendationNotFoundForUserException(RecommendationNotFoundForUserException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
