package com.basic.myspringboot.analysis;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class EmotionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emotion_id", nullable = false)
    private EmotionEnum emotion;

    @ManyToOne
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisVector analysis;

    @Column(nullable = false)
    private float score;
}
