package com.basic.myspringboot.diary;

import java.util.List;

public interface DiaryService {
    Diary createDiary(DiaryRequestDto dto);
    List<Diary> getAllDiaries();
    Diary getDiaryById(Long id);
    void deleteDiary(Long id);
}
