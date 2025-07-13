package com.basic.myspringboot.chat.repository;

import com.basic.myspringboot.chat.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findByUserId(Long userId);
    List<ChatSession> findAllByUserId(Long userId);
}
