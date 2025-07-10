package com.basic.myspringboot.common;

import lombok.*;

import java.time.ZonedDateTime;

/**
 * 공통 API 응답 클래스
 *
 * @param <T> 응답 데이터의 타입
 */
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

    // ✅ 성공 응답 생성
    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }

    // ✅ 실패 응답 생성
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}
