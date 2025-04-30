package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {
    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("AI Response: {}", aiResponse);
        return parseRecommendation(activity, aiResponse);
    }

    private Recommendation parseRecommendation(Activity activity, String aiResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            JsonNode analysisNode = objectMapper.readTree(jsonContent)
                    .path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();

            String[][] sections = {
                    {"overall", "Overall:"},
                    {"pace", "Pace:"},
                    {"heartRate", "Heart Rate:"},
                    {"caloriesBurned", "Calories Burned:"}
            };

            for (String[] section : sections) {
                addAnalysisSection(fullAnalysis, analysisNode, section[0], section[1]);
            }

            List<String> improvements = extractField("improvements", objectMapper.readTree(jsonContent).path("improvements"));
            List<String> suggestions = extractField("suggestions", objectMapper.readTree(jsonContent).path("suggestions"));
            List<String> safety = extractField("safety", objectMapper.readTree(jsonContent).path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Failed to parse recommendation", e);
            return new Recommendation();
        }
    }

    private List<String> extractField(String field, JsonNode fieldNode) {
        List<String> fieldList = new ArrayList<>();
        if (fieldNode.isArray()) {
            if (Objects.equals(field, "safety")) {
                fieldNode.forEach(element -> fieldList.add(element.asText()));
            }
            else {
                fieldNode.forEach(element -> {
                    String firstSubField = element.path(Objects.equals(field, "suggestions") ? "workout": "area").asText();
                    String secondSubField = element.path(Objects.equals(field, "suggestions") ? "description" : "recommendation").asText();
                    fieldList.add(String.format("%s: %s", firstSubField, secondSubField));
                });
            }
            return fieldList.isEmpty() ? Collections.singletonList("No specific suggestions") : fieldList ;
        }
        return Collections.singletonList("No specific suggestions");
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analyze this fitness activity and provide detailed recommendations in this particular format:
                {
                 "analysis": {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories burned analysis here",
                 },
                 "improvements" : [
                    {
                      "area": "Area name",
                      "recommendation": "Detailed recommendation for the area"
                    }
                 ],
                 "suggestions" : [
                    {
                        "workout": "Workout name",
                        "description": "detailed description of the workout"
                    }
                 ],
                 "safety" : [
                    "Safety point 1",
                    "Safety point 2"
                 ]
               }
               
               Analyse this activity:
               Activity Type: %s
               Duration: %d minutes
               Calories Burned: %d
               Additional Metrics: %s
               
               Provide Detailed analysis focusing on performance, improvements, next workout suggestions and safety guidelines.
               Ensure the response follows the EXACT JSON format shown above.
               """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics());
    }
}
