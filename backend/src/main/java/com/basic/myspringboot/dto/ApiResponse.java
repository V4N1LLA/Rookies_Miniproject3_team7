package com.basic.myspringboot.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String timestamp;
    private String feedback;

    // 성공 응답을 위한 정적 메서드
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("요청이 성공적으로 처리되었습니다.")
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    // 실패 응답을 위한 정적 메서드
    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> failure(T data, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, String feedback) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .feedback(feedback)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}