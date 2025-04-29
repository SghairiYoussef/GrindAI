package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value( "${gemini.api.key}")
    private String apiKey;

    @Value( "${gemini.api.url}")
    private String apiUrl;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String prompt) {
        Map<String, Object> request = Map.of(
                "contents", new Object[]{ Map.of(
                        "parts", new Object[]{
                        Map.of("text", prompt)
                        })
                });
        return webClient.post()
                .uri(apiUrl + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
