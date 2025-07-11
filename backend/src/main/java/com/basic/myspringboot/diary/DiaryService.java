package com.basic.myspringboot.diary;

import com.basic.myspringboot.auth.entity.User;
import java.util.List;

public interface DiaryService {

    /* --- 신규 메서드 --- */
    Diary createDiary(DiaryRequestDto dto, User user);
    List<Diary> getMyDiaries(User user);
    Diary getDiary(Long id, User user);   // 소유자 검증
    void deleteDiary(Long id, User user);

    /* --- 📌 레거시 컨트롤러 호환용 --- */
    @Deprecated
    Diary getDiaryById(Long id);          // ← EmotionAnalysisController용

    @Deprecated
    void saveDiary(Diary diary);          // ← EmotionAnalysisController용
}