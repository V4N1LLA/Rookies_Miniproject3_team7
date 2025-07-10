package com.basic.myspringboot.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private UserSummaryDto user;
}