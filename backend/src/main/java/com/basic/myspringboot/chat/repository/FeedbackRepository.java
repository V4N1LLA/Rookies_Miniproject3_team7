package com.basic.myspringboot.chat.repository;

import com.basic.myspringboot.chat.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // userId 기반 Feedback 조회
    Optional<Feedback> findByUserId(Long userId);

    // vector search query 기반 Feedback 조회 (예시)
    Optional<Feedback> findByChatMessage_Id(Long chatMessageId);

}
