package com.basic.myspringboot.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String email;     
    private String password;  
}
