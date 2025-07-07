package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    public EmotionAnalysisResponse analyze(EmotionAnalysisRequest request) {
        // 임시 mock 로직
        EmotionAnalysisResponse response = new EmotionAnalysisResponse();
        response.setDomainEmotion("SAD");

        response.addScore("SAD", 4.8f);
        response.addScore("FATIGUE", 3.2f);
        response.addScore("LONELY", 2.0f);

        return response;
    }
}
