package com.basic.myspringboot.diary;

import com.basic.myspringboot.auth.entity.User;
import java.util.List;

public interface DiaryService {
    Diary createDiary(DiaryRequestDto dto, Long userId);
    void saveDiary(Diary diary);
    List<Diary> getAllDiaries();
    Diary getDiaryById(Long id);
    void deleteDiary(Long id);

    List<Diary> getDiariesByUserId(Long userId);
}
