package com.basic.myspringboot.analysis;

import lombok.Getter;

@Getter
public class EmotionAnalysisRequest {
    private Long diaryId;
    private String content;
}
