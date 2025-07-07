package com.basic.myspringboot.analysis;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class AnalysisVector {

    @Id
    private Long analysisId; // FK → analysis_results (예정 테이블)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String vector;

    @Column(nullable = false)
    private int dim;

    @Column(nullable = false)
    private String domainEmotion;

    private LocalDateTime createdAt = LocalDateTime.now();
}
