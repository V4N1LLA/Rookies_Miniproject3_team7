package com.basic.myspringboot.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emotion_enum")
@Getter
@Setter
public class EmotionEnum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emotionId;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;
}
