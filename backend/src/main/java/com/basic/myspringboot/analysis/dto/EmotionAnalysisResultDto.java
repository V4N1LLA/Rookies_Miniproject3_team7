package com.basic.myspringboot.analysis.dto;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<EmotionScoreDto> scores;

    public static EmotionAnalysisResultDto from(EmotionAnalysisResult entity) {
        String messageText = null;
        Long diaryId = null;

        if (entity.getDiary() != null) {
            diaryId = entity.getDiary().getDiaryId();

            // 공감 메시지 Lazy 로딩 강제 접근
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
                //.scores(
                //        entity.getScores().stream()
                //                .map(EmotionScoreDto::fromEntity)
                //                .collect(Collectors.toList())
                //)
                .build();
    }
}
