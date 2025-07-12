package com.basic.myspringboot.analysis.entity;

import com.basic.myspringboot.diary.Diary;
import com.basic.myspringboot.message.EncouragementMessage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "emotion_analysis_result")
public class EmotionAnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    @Column(name = "domain_emotion", nullable = false)
    private String domainEmotion;

    private int dim;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<EmotionScore> scores;

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AnalysisVector> vectors;

    @OneToOne(mappedBy = "analysisResult", fetch = FetchType.LAZY)
    private Diary diary;

    // ✅ 공감 메시지 연관관계 추가
    @OneToOne(mappedBy = "analysisResult", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EncouragementMessage encouragementMessage;
}
