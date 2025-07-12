package com.basic.myspringboot.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id")
    private ChatSession chatSession;

    private String sender; // USER or BOT

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "chatMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Feedback feedback;

    public String getMessage() {
        return this.content; // content 필드를 message로 사용 중인 경우
    }

    public LocalDateTime getTimestamp() {
        return this.createdAt; // createdAt 필드를 timestamp로 사용 중인 경우
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}