package com.basic.myspringboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequestDto {

    @NotBlank(message = "username은 필수입니다.")
    private String username;

    @NotBlank(message = "password는 필수입니다.")
    private String password;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수입니다.")
    private String email;
}
