package com.basic.myspringboot.analysis.repository;

import com.basic.myspringboot.analysis.entity.EmotionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmotionEnumRepository extends JpaRepository<EmotionEnum, Long> {
    Optional<EmotionEnum> findByCode(String code);  // 감정 코드(SAD, HAPPY 등)로 조회
}
