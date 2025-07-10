package com.basic.myspringboot.diary;

import com.basic.myspringboot.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /* 내 글 목록 (최신순) */
    List<Diary> findByUserOrderByTimestampDesc(User user);

    /* 하루 한 편 제한 체크 */
    Optional<Diary> findByUserAndTimestampBetween(
            User user, LocalDateTime start, LocalDateTime end);
}