// 📁 service/EmotionScoreService.java
package com.basic.myspringboot.analysis.service;

import com.basic.myspringboot.analysis.dto.EmotionScoreDto;
import com.basic.myspringboot.analysis.entity.EmotionScore;
import com.basic.myspringboot.analysis.repository.EmotionScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmotionScoreService {

    private final EmotionScoreRepository emotionScoreRepository;

    public List<EmotionScoreDto> findByAnalysisId(Long analysisId) {
        List<EmotionScore> scores = emotionScoreRepository.findByAnalysisResult_AnalysisId(analysisId);
        return scores.stream()
                .map(EmotionScoreDto::fromEntity)
                .collect(Collectors.toList());
    }
}
