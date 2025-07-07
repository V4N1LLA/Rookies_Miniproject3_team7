package com.basic.myspringboot.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    @Override
    public Diary createDiary(DiaryRequestDto dto) {
        LocalDateTime parsedTime = LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);

        Diary diary = Diary.builder()
                .userId(1L)  // JWT 기반으로 추후 대체 예정
                .title(dto.getTitle())
                .content(dto.getContent())
                .timestamp(parsedTime)
                .build();

        return diaryRepository.save(diary);
    }

    @Override
    public List<Diary> getAllDiaries() {
        return diaryRepository.findAll();
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
