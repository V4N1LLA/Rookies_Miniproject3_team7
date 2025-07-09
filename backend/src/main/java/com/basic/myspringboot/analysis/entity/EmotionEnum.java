package com.basic.myspringboot.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emotion_enum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionEnum {

    @Id
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    // 기본 생성자, equals, hashCode 필요 시 추가
}
