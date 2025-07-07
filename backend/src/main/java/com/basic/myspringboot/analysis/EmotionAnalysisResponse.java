package com.basic.myspringboot.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmotionAnalysisResponse {
    private String domainEmotion;

    @Builder.Default
    private Map<String, Float> scores = new HashMap<>();

    private double[] vector;
    private int dim;
    private LocalDateTime createdAt;

    public void addScore(String emotion, Float score) {
        scores.put(emotion, score);
    }
}
