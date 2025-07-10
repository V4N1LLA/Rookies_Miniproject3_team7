package com.basic.myspringboot.analysis.repository;

import com.basic.myspringboot.analysis.entity.EmotionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmotionScoreRepository extends JpaRepository<EmotionScore, Long> {

    // ✅ analysis_id 기준으로 감정 점수 리스트 조회
    List<EmotionScore> findByAnalysisResult_AnalysisId(Long analysisId);
}
