package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final EmotionClient emotionClient;
    private final EmotionAnalysisResultRepository resultRepository;
    private final EmotionScoreRepository scoreRepository;
    private final AnalysisVectorRepository vectorRepository;

    public EmotionAnalysisResult analyzeAndSave(String content) {
        // 1. FastAPI로 감정 분석 요청
        EmotionAnalysisResponse response = emotionClient.requestAnalysis(content);

        // 2. EmotionAnalysisResult 저장
        EmotionAnalysisResult result = EmotionAnalysisResult.builder()
                .domainEmotion(response.getDomainEmotion())
                .dim(response.getDim())
                .createdAt(response.getCreatedAt() != null ? response.getCreatedAt() : LocalDateTime.now())
                .build();
        resultRepository.saveAndFlush(result);  // ✅ 반드시 saveAndFlush()로 영속화

        // 3. EmotionScore 저장
        for (Map.Entry<String, Float> entry : response.getScores().entrySet()) {
            EmotionScore score = EmotionScore.builder()
                    .analysisResult(result)  // ✅ 필드명 주의
                    .emotion(entry.getKey())
                    .score(entry.getValue())
                    .build();
            scoreRepository.save(score);
        }

        // 4. AnalysisVector 저장 (배열 전체를 문자열로 저장)
        String vectorStr = Arrays.toString(response.getVector());
        AnalysisVector vectorEntity = AnalysisVector.builder()
                .analysisResult(result)
                .vector(vectorStr)
                .dim(response.getDim())
                .domainEmotion(response.getDomainEmotion())
                .createdAt(response.getCreatedAt())
                .build();
        vectorRepository.save(vectorEntity);

        return result;
    }
}
