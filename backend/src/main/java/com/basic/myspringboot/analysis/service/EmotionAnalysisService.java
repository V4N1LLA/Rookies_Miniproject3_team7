package com.basic.myspringboot.analysis.service;

import com.basic.myspringboot.analysis.client.EmotionClient;
import com.basic.myspringboot.analysis.dto.EmotionAnalysisResultDto;
import com.basic.myspringboot.analysis.dto.EmotionResponseDto;
import com.basic.myspringboot.analysis.entity.*;
import com.basic.myspringboot.analysis.repository.*;
import com.basic.myspringboot.diary.Diary;
import com.basic.myspringboot.diary.DiaryRepository;
import com.basic.myspringboot.message.EncouragementMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final EmotionAnalysisResultRepository resultRepository;
    private final EmotionScoreRepository scoreRepository;
    private final AnalysisVectorRepository vectorRepository;
    private final EmotionEnumRepository emotionEnumRepository;
    private final DiaryRepository diaryRepository;
    private final EmotionClient emotionClient;

    // ✅ 감정 분석 후 저장
    public EmotionAnalysisResult analyzeAndSave(String content) {
        EmotionResponseDto response = emotionClient.requestAnalysis(content);

        EmotionAnalysisResult result = EmotionAnalysisResult.builder()
                .domainEmotion(response.getDomainEmotion())
                .dim(response.getDim())
                .build(); // createdAt은 @PrePersist로 자동 설정
        resultRepository.saveAndFlush(result);

        for (Map.Entry<String, Float> entry : response.getScores().entrySet()) {
            EmotionEnum emotionEnum = emotionEnumRepository.findByCode(entry.getKey())
                    .orElseGet(() -> {
                        EmotionEnum newEnum = new EmotionEnum();
                        newEnum.setCode(entry.getKey());
                        newEnum.setLabel(entry.getKey());
                        return emotionEnumRepository.save(newEnum);
                    });

            EmotionScore score = EmotionScore.builder()
                    .analysisResult(result)
                    .score(entry.getValue())
                    .emotionEnum(emotionEnum)
                    .build();
            scoreRepository.save(score);
        }

        String vectorStr = response.getVector().toString();
        AnalysisVector vector = AnalysisVector.builder()
                .analysisResult(result)
                .vector(vectorStr)
                .dim(response.getDim())
                .domainEmotion(response.getDomainEmotion())
                .createdAt(response.getCreatedAt())
                .build();
        vectorRepository.save(vector);

        return result;
    }

    // ✅ 감정 분석 후 DTO 반환
    public EmotionAnalysisResultDto analyzeAndReturnDto(String content) {
        EmotionAnalysisResult saved = analyzeAndSave(content);
        return EmotionAnalysisResultDto.from(saved);
    }

    // ✅ 일기 ID 기반 단건 조회 (DTO 반환)
    public EmotionAnalysisResultDto getByDiaryId(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다. id=" + diaryId));

        EmotionAnalysisResult result = diary.getAnalysisResult();
        if (result == null) {
            throw new IllegalArgumentException("해당 일기에 감정 분석 결과가 없습니다.");
        }

        return EmotionAnalysisResultDto.from(result);
    }

    // ✅ 전체 조회 (모든 사용자)
    public List<EmotionAnalysisResultDto> getAllAnalysisResultDtos() {
        return resultRepository.findAll()
                .stream()
                .map(EmotionAnalysisResultDto::from)
                .collect(Collectors.toList());
    }

    // ✅ 로그인한 사용자 기준 감정 분석 결과 전체 조회
    public List<EmotionAnalysisResultDto> getAnalysisResultsByUserId(Long userId) {
        List<Diary> diaries = diaryRepository.findByUserId(userId);

        return diaries.stream()
                .filter(d -> d.getAnalysisResult() != null)
                .map(diary -> {
                    EmotionAnalysisResult result = diary.getAnalysisResult();
                    EncouragementMessage message = diary.getEncouragementMessage();

                    return EmotionAnalysisResultDto.builder()
                            .analysisId(result.getAnalysisId())
                            .diaryId(diary.getDiaryId())
                            .domainEmotion(result.getDomainEmotion())
                            .dim(result.getDim())
                            .createdAt(result.getCreatedAt())
                            .message(message != null ? message.getText() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
