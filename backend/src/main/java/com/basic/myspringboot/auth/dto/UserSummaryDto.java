package com.basic.myspringboot.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class UserSummaryDto {
    private Long userId;
    private String email;
    private String name;
}
