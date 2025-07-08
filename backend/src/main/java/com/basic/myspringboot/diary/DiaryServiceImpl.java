package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final EmotionAnalysisService emotionAnalysisService;

    @Override
    public Diary createDiary(DiaryRequestDto dto) {
        // 감정 분석
        EmotionAnalysisResult analysisResult = emotionAnalysisService.analyzeAndSave(dto.getContent());

        Diary diary = Diary.builder()
                .userId(1L)  // 추후 JWT에서 대체
                .title(dto.getTitle())
                .content(dto.getContent())
                .timestamp(dto.getTimestamp())
                // .analysisResult(analysisResult)  // **감정 분석 제외**
                .build();

        return diaryRepository.save(diary);
    }

    @Override
    public List<Diary> getAllDiaries() {
        return diaryRepository.findAllWithAnalysis(); // 🔥 fetch join
    }

    @Override
    public Diary getDiaryById(Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 다이어리를 찾을 수 없습니다: " + id));
    }

    @Override
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }
}
