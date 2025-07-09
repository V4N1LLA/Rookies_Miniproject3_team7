package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.diary.exception.DuplicateDiaryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    // ✅ 감정 분석 없이 일기만 등록
    @Override
    public Diary createDiary(DiaryRequestDto dto, Long userId) {
        LocalDateTime timestamp = dto.getTimestamp();
        checkIfDiaryAlreadyExists(userId, timestamp);

        Diary diary = Diary.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .timestamp(timestamp)
                .build();

        return diaryRepository.save(diary);
    }

    @Override
    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    @Override
    public List<Diary> getAllDiaries() {
        return diaryRepository.findAllWithAnalysis();
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

    // ✅ 하루 하나 작성 제약 체크
    private void checkIfDiaryAlreadyExists(Long userId, LocalDateTime timestamp) {
        LocalDateTime startOfDay = timestamp.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Optional<Diary> existing = diaryRepository.findByUserIdAndTimestampBetween(userId, startOfDay, endOfDay);
        if (existing.isPresent()) {
            throw new DuplicateDiaryException("해당 날짜에 이미 작성된 일기가 있습니다.");
        }
    }
}