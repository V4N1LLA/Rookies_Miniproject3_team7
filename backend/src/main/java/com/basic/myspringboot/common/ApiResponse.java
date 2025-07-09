package com.basic.myspringboot.common;

import lombok.*;
import java.time.ZonedDateTime;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String timestamp;

    public static <T> ApiResponse<T> ok(T data, String msg) {
        return ApiResponse.<T>builder()
                .success(true).data(data).message(msg)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
    public static <T> ApiResponse<T> fail(String msg) {
        return ApiResponse.<T>builder()
                .success(false).data(null).message(msg)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}