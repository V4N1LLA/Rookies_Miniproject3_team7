package com.basic.myspringboot.auth.controller;

import com.basic.myspringboot.auth.dto.*;
import com.basic.myspringboot.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "회원가입 · 로그인 · 정보수정 · 탈퇴")
public class AuthController {

    private final AuthService authService;

    /* ---------- 회원가입 ---------- */
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        return authService.signup(dto);
    }

    /* ---------- 로그인 ---------- */
    @Operation(summary = "로그인(토큰 발급)")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }

    /* ---------- 회원정보 수정 ---------- */
    @Operation(summary = "회원 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(
            Authentication authentication,
            @RequestBody UserUpdateRequestDto dto) {

        return authService.updateUser(authentication.getName(), dto);
    }

    /* ---------- 회원 탈퇴 ---------- */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(
            Authentication authentication,
            @RequestBody DeleteRequestDto dto) {

        return authService.deleteUser(authentication.getName(), dto);
    }
}