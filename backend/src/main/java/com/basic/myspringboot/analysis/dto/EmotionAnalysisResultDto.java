package com.basic.myspringboot.analysis.dto;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmotionAnalysisResultDto {

    private Long analysisId;
    private String domainEmotion;
    private int dim;
    private LocalDateTime createdAt;

    // ✅ 정적 팩토리 메서드
    public static EmotionAnalysisResultDto from(EmotionAnalysisResult entity) {
        return EmotionAnalysisResultDto.builder()
                .analysisId(entity.getAnalysisId())
                .domainEmotion(entity.getDomainEmotion())
                .dim(entity.getDim())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
