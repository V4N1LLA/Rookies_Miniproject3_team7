package com.basic.myspringboot.analysis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "emotion_scores")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emotion;

    private Float score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    @JsonBackReference
    private EmotionAnalysisResult analysisResult;
}
