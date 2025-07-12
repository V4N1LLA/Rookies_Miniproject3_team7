package com.basic.myspringboot.diary;

import java.util.Map;

public record DiaryDto(
        Long diaryid,
        String title,
        String content,
        String timestamp,
        Map<String,Object> analysisResult
) {
    public static DiaryDto from(Diary d) {
        Map<String,Object> ar = null;
        if (d.getAnalysisResult() != null) {
            ar = Map.of(
                    "domainEmotion", d.getAnalysisResult().getDomainEmotion(),
                    "dim", d.getAnalysisResult().getDim()
            );
        }
        return new DiaryDto(
                d.getDiaryId(),
                d.getTitle(),
                d.getContent(),
                d.getTimestamp().toString(),
                ar
        );
    }
}