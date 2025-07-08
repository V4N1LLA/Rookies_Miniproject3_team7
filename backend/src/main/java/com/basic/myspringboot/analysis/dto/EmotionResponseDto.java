package com.basic.myspringboot.analysis.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class EmotionResponseDto {
    private String domainEmotion;
    private Map<String, Float> scores;
    private List<Float> vector;
    private int dim;
    private LocalDateTime createdAt;
}
