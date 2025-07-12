package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.diary.DuplicateDiaryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service @RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepo;

    @Override
    public Diary createDiary(DiaryRequestDto dto, User user) {

        LocalDateTime ts = dto.getTimestamp() != null
                ? dto.getTimestamp() : LocalDateTime.now();

        /* 하루 1편 제약 */
        LocalDateTime s = ts.toLocalDate().atStartOfDay();
        LocalDateTime e = s.plusDays(1).minusNanos(1);
        diaryRepo.findByUserAndTimestampBetween(user, s, e)
                 .ifPresent(d -> { throw new DuplicateDiaryException("이미 작성된 일기가 있습니다."); });

        Diary diary = Diary.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .timestamp(ts)
                .build();

        return diaryRepo.save(diary);
    }

    @Override
    public List<Diary> getMyDiaries(User user) {
        return diaryRepo.findByUserOrderByTimestampDesc(user);
    }

    @Override
    public Diary getDiary(Long id, User user) {
        Diary d = diaryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Diary not found"));
        if (!d.getUser().getId().equals(user.getId())) {
            throw new ForbiddenDiaryException();
        }
        return d;
    }

    @Override
    public void deleteDiary(Long id, User user) {
        Diary d = getDiary(id, user);
        diaryRepo.delete(d);
    }

    @Override @Deprecated
    public Diary getDiaryById(Long id) {
        return diaryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Diary not found"));
    }

    @Override
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }

    @Override
    public List<Diary> getDiariesByUserId(Long userId) {
        return diaryRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    private void checkIfDiaryAlreadyExists(Long userId, LocalDateTime timestamp) {
        // ✅ 미래 날짜 작성 방지
        if (timestamp.toLocalDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("미래 날짜에는 일기를 작성할 수 없습니다.");
        }

        // ✅ 하루 하나 작성 제약
        LocalDateTime startOfDay = timestamp.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Optional<Diary> existing = diaryRepository.findByUserIdAndTimestampBetween(userId, startOfDay, endOfDay);
        if (existing.isPresent()) {
            throw new DuplicateDiaryException("해당 날짜에 이미 작성된 일기가 있습니다.");
        }
    }

}