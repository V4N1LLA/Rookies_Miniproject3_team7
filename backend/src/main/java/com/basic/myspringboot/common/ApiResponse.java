package com.basic.myspringboot.common;

import lombok.*;

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
    private String message;
    private T data;
    private String timestamp;

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> ofSuccess(T data, String message, String timestamp) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(timestamp)
                .build();
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> ofError(String message, String timestamp) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(timestamp)
                .build();
    }
}
