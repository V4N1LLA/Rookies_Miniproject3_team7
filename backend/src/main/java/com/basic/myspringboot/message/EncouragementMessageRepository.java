package com.basic.myspringboot.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EncouragementMessageRepository extends JpaRepository<EncouragementMessage, Long> {
    Optional<EncouragementMessage> findByDiary_DiaryId(Long diaryId);
}
