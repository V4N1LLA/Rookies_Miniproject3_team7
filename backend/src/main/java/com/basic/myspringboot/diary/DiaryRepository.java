package com.basic.myspringboot.diary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends CrudRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.analysisResult")
    List<Diary> findAllWithAnalysis();

    // ✅ 하루에 하나 작성 제약을 위한 쿼리
    Optional<Diary> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
