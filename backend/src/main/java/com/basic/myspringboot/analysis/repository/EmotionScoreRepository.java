package com.basic.myspringboot.analysis.repository;

import com.basic.myspringboot.analysis.entity.EmotionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionScoreRepository extends JpaRepository<EmotionScore, Long> {
}
