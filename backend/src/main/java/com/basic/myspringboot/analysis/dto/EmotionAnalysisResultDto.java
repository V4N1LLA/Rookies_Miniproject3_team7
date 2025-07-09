package com.basic.myspringboot.analysis.dto;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionAnalysisResultDto {

    private Long analysisId;
    private String domainEmotion;
    private int dim;
    private LocalDateTime createdAt;
    private String message;
    private Long diaryId;

    public static EmotionAnalysisResultDto from(EmotionAnalysisResult entity) {
        String messageText = null;
        Long diaryId = null;

        if (entity.getDiary() != null) {
            diaryId = entity.getDiary().getDiaryId();

            // Lazy 로딩 강제 초기화
            if (entity.getDiary().getEncouragementMessage() != null) {
                messageText = entity.getDiary().getEncouragementMessage().getText();
            }
        }

        return EmotionAnalysisResultDto.builder()
                .analysisId(entity.getAnalysisId())
                .domainEmotion(entity.getDomainEmotion())
                .dim(entity.getDim())
                .createdAt(entity.getCreatedAt())
                .message(messageText)
                .diaryId(diaryId)
                .build();
    }
}