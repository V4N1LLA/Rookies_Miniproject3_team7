package com.basic.myspringboot.chat.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VectorDbClient {

    private final RestTemplate restTemplate;

    @Value("${faiss.api.url}")
    private String faissApiUrl;

    public void saveEmbedding(Long analysisId, String embeddingJson, int dimension, String domainEmotion) {
        String url = faissApiUrl + "/save";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // ✅ headers.setBearerAuth(jwtToken); 제거

        Map<String, Object> body = new HashMap<>();
        body.put("analysis_id", analysisId);
        body.put("vector", embeddingJson);
        body.put("dim", dimension);
        body.put("domain_emotion", domainEmotion);

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("[VectorDB-FAISS] 저장 완료 | analysisId: {} | status: {}", analysisId, response.getStatusCode());
        } catch (RestClientException e) {
            log.error("[VectorDB-FAISS] 저장 실패: {}", e.getMessage());
        }
    }
}
