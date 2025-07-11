package com.basic.myspringboot.message;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.diary.Diary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "encouragement_messages")
public class EncouragementMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @OneToOne
    @JoinColumn(name = "analysis_id")
    private EmotionAnalysisResult analysisResult;

    @Column(name = "emotion", nullable = false)
    private String emotion;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
}
