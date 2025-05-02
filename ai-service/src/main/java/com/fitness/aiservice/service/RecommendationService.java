package com.fitness.aiservice.service;

import com.fitness.aiservice.exception.RecommendationNotFoundForActivityException;
import com.fitness.aiservice.exception.RecommendationNotFoundForUserException;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
        if (recommendations.isEmpty()) {
            throw new RecommendationNotFoundForUserException("No recommendations found for userId: " + userId);
        }
        return recommendations;
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RecommendationNotFoundForActivityException("No recommendation found for activityId: " + activityId));
    }
}
