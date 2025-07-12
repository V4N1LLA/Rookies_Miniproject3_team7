package com.basic.myspringboot.chat.repository;

import com.basic.myspringboot.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSessionId(Long sessionId);
}
