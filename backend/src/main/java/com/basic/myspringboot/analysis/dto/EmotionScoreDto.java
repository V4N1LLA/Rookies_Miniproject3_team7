package com.basic.myspringboot.analysis.dto;

import com.basic.myspringboot.analysis.entity.EmotionScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode 포함
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionScoreDto {

    private String code;   // 예: "SAD"
    private String label;  // 예: "슬픔"
    private float score;   // 예: 0.876

    public static EmotionScoreDto fromEntity(EmotionScore score) {
        return EmotionScoreDto.builder()
                .code(score.getEmotionEnum().getCode())
                .label(score.getEmotionEnum().getLabel())
                .score(score.getScore())
                .build();
    }
}
