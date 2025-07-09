package com.basic.myspringboot.message;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class MessageClient {

    @Value("${emotion.api.url}")
    private String baseUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String requestMessage(String emotion, String content) {
        Map<String, String> requestBody = Map.of(
                "emotion", emotion,
                "content", content
        );

        return webClient.post()
                .uri("/api/message")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(MessageResponse.class)
                .map(MessageResponse::getMessage)
                .onErrorReturn("공감 메시지 생성 실패")  // ✅ 예외 처리 기본 메시지
                .block();
    }

    @Getter
    @Setter
    public static class MessageResponse {
        private String message;
    }
}
