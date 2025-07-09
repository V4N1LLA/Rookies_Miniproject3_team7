package com.basic.myspringboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VectorDbClient {

    private final RestTemplate restTemplate;

    @Value("${faiss.api.url}")
    private String faissApiUrl; // ex) http://localhost:5000/api/vectors

    /**
     * FAISS 서버에 embedding vector 저장
     */
    public void saveEmbedding(Long analysisId, String embeddingJson, int dimension, String domainEmotion, String jwtToken) {

        String url = faissApiUrl + "/save"; // ⚠️ url 변수 누락 수정

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        Map<String, Object> body = new HashMap<>();
        body.put("analysis_id", analysisId);
        body.put("vector", embeddingJson);
        body.put("dim", dimension);
        body.put("domain_emotion", domainEmotion);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("[VectorDB-FAISS] 저장 완료 | analysisId: " + analysisId + " | status: " + response.getStatusCode());
    }
}
