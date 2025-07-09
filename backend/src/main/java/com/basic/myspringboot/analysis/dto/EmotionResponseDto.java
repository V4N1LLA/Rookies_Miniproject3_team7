package com.basic.myspringboot.analysis.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class EmotionResponseDto {
    @JsonProperty("domain_emotion")
    private String domainEmotion;

    @JsonProperty("scores")
    private Map<String, Float> scores;

    @JsonProperty("vector")
    private List<Float> vector;

    private int dim;

    @JsonProperty("encouragement_message")
    private String encouragementMessage;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    // getters & setters or lombok
}
