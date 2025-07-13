package com.basic.myspringboot.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatVectorResponse {
    private String result; // FastAPI 응답이 { "result": "..." } 형태일 때
}
