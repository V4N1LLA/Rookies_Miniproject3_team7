package com.basic.myspringboot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LangServeClient {

    // 기존 @Value 주입 제거
    @Autowired
    private RestTemplate restTemplate;

    public String getAIResponse(String prompt) {
        // 🔧 OpenAI 호출 대신 mock 응답 반환
        return "This is a mock AI response for prompt: " + prompt;
    }

    public String callChatbotAPI(String message) {
        try {
            String url = "http://fastapi-chatbot:8000/chat";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode requestJson = mapper.createObjectNode();
            requestJson.put("message", message);

            HttpEntity<String> request = new HttpEntity<>(requestJson.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("LangServe 호출 실패", e);
            return "죄송합니다. 현재 AI 서버가 응답하지 않습니다.";
        }
    }

}

// @Component
// @RequiredArgsConstructor
// public class LangServeClient {

// @Value("${openai.api.key}")
// private String apiKey;

// private final RestTemplate restTemplate;
// private final ObjectMapper objectMapper;

// public String getAIResponse(String userMessage) {
// String url = "https://api.openai.com/v1/chat/completions";

// HttpHeaders headers = new HttpHeaders();
// headers.setContentType(MediaType.APPLICATION_JSON);
// headers.setBearerAuth(apiKey);

// String requestBody = "{\n" +
// " \"model\": \"gpt-3.5-turbo\",\n" +
// " \"messages\": [\n" +
// " {\"role\": \"system\", \"content\": \"너는 감정 공감 AI 챗봇이다.\"},\n" +
// " {\"role\": \"user\", \"content\": \"" + userMessage + "\"}\n" +
// " ]\n" +
// "}";

// HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
// ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
// entity, String.class);

// try {
// JsonNode root = objectMapper.readTree(response.getBody());
// String content =
// root.get("choices").get(0).get("message").get("content").asText();
// return content;
// } catch (Exception e) {
// throw new RuntimeException("AI 응답 파싱 실패", e);
// }
// }
// }
