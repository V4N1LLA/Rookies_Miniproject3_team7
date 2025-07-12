package com.basic.myspringboot.chat.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LangServeClient {

    @Autowired
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAIResponse(String prompt) {
        String url = "http://fastapi-chatbot:8000/api/chat"; // ✅ 올바른 컨테이너 내부 주소

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "sessionId", 0,
                "sender", "user",
                "content", prompt
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("response").asText(); // ✅ FastAPI 응답 형식 { "response": "...텍스트..." }

        } catch (RestClientException e) {
            log.error("FastAPI 챗봇 서버 호출 실패: {}", e.getMessage());
            return "죄송합니다. 현재 AI 서버가 응답하지 않습니다.";
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
            return "죄송합니다. 응답 처리 중 문제가 발생했습니다.";
        }
    }
}
