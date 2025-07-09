package com.basic.myspringboot.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateRequestDto {
    @Email private String email;   // 선택 수정
    private String password;       // 선택 수정
    private String name;           // 선택 수정
}
