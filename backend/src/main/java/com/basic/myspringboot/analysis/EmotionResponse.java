// analysis/EmotionResponse.java
package com.basic.myspringboot.analysis;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Data
public class EmotionResponse {
    private String domainEmotion;
    private Map<String, Double> scores;
    private List<Double> vector;
    private int dim;
    private LocalDateTime createdAt;
}
