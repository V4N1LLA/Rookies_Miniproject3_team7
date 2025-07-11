package com.basic.myspringboot.diary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends CrudRepository<Diary, Long> {

    List<Diary> findByUserId(Long userId);

    // ✅ 감정 분석 결과도 같이 조회 (fetch join)
    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.analysisResult")
    List<Diary> findAllWithAnalysis();

    // ✅ 사용자 ID로 해당 사용자의 모든 일기 조회
    List<Diary> findByUserIdOrderByTimestampDesc(Long userId);

    // ✅ 하루에 하나 작성 제약을 위한 쿼리
    Optional<Diary> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // ✅ 로그인한 사용자의 일기.
    @Query("SELECT d FROM Diary d LEFT JOIN FETCH d.analysisResult ar LEFT JOIN FETCH ar.encouragementMessage WHERE d.userId = :userId")
    List<Diary> findByUserIdWithAnalysis(Long userId);

}
