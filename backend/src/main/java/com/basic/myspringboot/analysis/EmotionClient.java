// EmotionClient.java
package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmotionClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public EmotionAnalysisResponse requestAnalysis(String content) {
        EmotionRequest request = new EmotionRequest(content);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmotionRequest> entity = new HttpEntity<>(request, headers);

        // ✅ 여기 수정
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://host.docker.internal:8000/analyze", entity, Map.class
        );

        Map<String, Object> body = response.getBody();

        Map<String, Double> doubleScores = (Map<String, Double>) body.get("scores");
        Map<String, Float> floatScores = new HashMap<>();
        doubleScores.forEach((k, v) -> floatScores.put(k, v.floatValue()));

        List<Double> vectorList = (List<Double>) body.get("vector");
        double[] vector = vectorList.stream().mapToDouble(Double::doubleValue).toArray();

        return EmotionAnalysisResponse.builder()
                .domainEmotion((String) body.get("domainEmotion"))
                .scores(floatScores)
                .vector(vector)
                .dim((int) body.get("dim"))
                .createdAt(LocalDateTime.parse((String) body.get("createdAt")))
                .build();
    }
}
