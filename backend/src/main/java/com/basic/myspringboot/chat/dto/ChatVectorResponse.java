package com.basic.myspringboot.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatVectorResponse {
    private String result; // ✅ FastAPI 응답 필드명에 맞게 수정
}
