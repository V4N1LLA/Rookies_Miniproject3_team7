package com.basic.myspringboot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequest {
    private String feedback; // 예: "HELPFUL"
}
