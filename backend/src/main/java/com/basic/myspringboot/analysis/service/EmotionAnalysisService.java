package com.basic.myspringboot.analysis.service;

import com.basic.myspringboot.analysis.client.EmotionClient;
import com.basic.myspringboot.analysis.dto.EmotionResponseDto;
import com.basic.myspringboot.analysis.entity.*;
import com.basic.myspringboot.analysis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final EmotionAnalysisResultRepository resultRepository;
    private final EmotionScoreRepository scoreRepository;
    private final AnalysisVectorRepository vectorRepository;
    private final EmotionEnumRepository emotionEnumRepository;

    private final EmotionClient emotionClient;

    public EmotionAnalysisResult analyzeAndSave(String content) {
        // 1. FastAPI로 감정 분석 요청
        EmotionResponseDto response = emotionClient.requestAnalysis(content);

        // 2. EmotionAnalysisResult 저장
        EmotionAnalysisResult result = EmotionAnalysisResult.builder()
                .domainEmotion(response.getDomainEmotion())
                .dim(response.getDim())
                .createdAt(response.getCreatedAt() != null ? response.getCreatedAt() : LocalDateTime.now())
                .build();
        resultRepository.saveAndFlush(result);

        // 3. EmotionScore 저장
        for (Map.Entry<String, Float> entry : response.getScores().entrySet()) {
            EmotionEnum emotionEnum = emotionEnumRepository.findByCode(entry.getKey())
                    .orElseGet(() -> {
                        EmotionEnum newEnum = new EmotionEnum();
                        newEnum.setCode(entry.getKey());
                        newEnum.setLabel(entry.getKey());
                        return emotionEnumRepository.save(newEnum);
                    });

            EmotionScore score = EmotionScore.builder()
                    .analysisResult(result)
                    .score(entry.getValue())
                    .emotionEnum(emotionEnum)
                    .build();
            scoreRepository.save(score);
        }

        // 4. AnalysisVector 저장
        String vectorStr = response.getVector().toString(); // List<Float> → 문자열
        AnalysisVector vector = AnalysisVector.builder()
                .analysisResult(result)
                .vector(vectorStr)
                .dim(response.getDim())
                .domainEmotion(response.getDomainEmotion())
                .createdAt(response.getCreatedAt())
                .build();
        vectorRepository.save(vector);

        return result;
    }
}
