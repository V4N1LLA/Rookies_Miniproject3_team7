package com.basic.myspringboot.analysis.client;

import com.basic.myspringboot.analysis.dto.EmotionRequestDto;
import com.basic.myspringboot.analysis.dto.EmotionResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmotionClient {

    @Value("${emotion.api.url}")
    private String baseUrl;  // ✅ 환경 변수에서 주입됨

    private WebClient webClient;

    @PostConstruct
    public void init() {
        System.out.println("🚀 Emotion API Base URL: " + baseUrl);
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public EmotionResponseDto requestAnalysis(String content) {
        EmotionRequestDto request = new EmotionRequestDto(content);

        EmotionResponseDto response = webClient.post()
                .uri("/api/emotion")  // ✅ 변경됨
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmotionResponseDto.class)
                .block();

        System.out.println("🧪 Emotion API 응답: " + response);

        return response;
    }
}
