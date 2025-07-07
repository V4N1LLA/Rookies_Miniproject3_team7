package com.basic.myspringboot.analysis;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class EmotionEnum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emotionId;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;
}
