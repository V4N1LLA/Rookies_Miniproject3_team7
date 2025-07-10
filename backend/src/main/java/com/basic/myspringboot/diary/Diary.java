package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.auth.entity.User;
import com.basic.myspringboot.message.EncouragementMessage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Diary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    /* 로그인 사용자 FK (N:1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    /* 감정 분석 결과 */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "analysis_id")
    private EmotionAnalysisResult analysisResult;

    /* 공감 메시지 */
    @OneToOne(mappedBy = "diary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EncouragementMessage encouragementMessage;

    @PrePersist
    void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
