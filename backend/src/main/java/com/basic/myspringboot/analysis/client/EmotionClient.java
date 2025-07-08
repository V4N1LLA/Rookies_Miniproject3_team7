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
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public EmotionResponseDto requestAnalysis(String content) {
        EmotionRequestDto request = new EmotionRequestDto(content);

        return webClient.post()
                .uri("/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmotionResponseDto.class)
                .block();  // 동기 호출
    }
}
