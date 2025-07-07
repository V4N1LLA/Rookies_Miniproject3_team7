package com.basic.myspringboot.analysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionAnalysisResultRepository extends JpaRepository<EmotionAnalysisResult, Long> {
}
