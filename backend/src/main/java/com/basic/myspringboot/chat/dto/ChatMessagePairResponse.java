package com.basic.myspringboot.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePairResponse {
    private ChatMessageResponse userMessage;
    private ChatMessageResponse systemMessage;
}
