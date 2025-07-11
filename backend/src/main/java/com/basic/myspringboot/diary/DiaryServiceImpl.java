package com.basic.myspringboot.diary;

import com.basic.myspringboot.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override @Deprecated
    public void saveDiary(Diary diary) {
        diaryRepo.save(diary);
    }
}