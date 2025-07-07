package com.basic.myspringboot.analysis;

import com.basic.myspringboot.analysis.EmotionRequestDto;
import com.basic.myspringboot.analysis.EmotionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000") // Python 서버 주소
            .build();

    public EmotionResponseDto analyzeEmotion(String content) {
        EmotionRequestDto request = new EmotionRequestDto(content);

        return webClient.post()
                .uri("/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmotionResponseDto.class)
                .block();  // 동기 처리
    }
}
