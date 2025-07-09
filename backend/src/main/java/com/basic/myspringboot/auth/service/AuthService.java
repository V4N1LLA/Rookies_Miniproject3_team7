package com.basic.myspringboot.auth.service;

import com.basic.myspringboot.auth.dto.*;
import com.basic.myspringboot.auth.entity.User;
import com.basic.myspringboot.auth.repository.UserRepository;
import com.basic.myspringboot.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository   userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /* ---------- 회원가입 ---------- */
    public ResponseEntity<?> signup(SignupRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 존재하는 이메일입니다."));
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .build();
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "회원가입 성공"));
    }

    /* ---------- 로그인 ---------- */
    public ResponseEntity<?> login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "이메일 또는 비밀번호가 일치하지 않습니다."));
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        Map<String, Object> responseBody = Map.of(
                "success", true,
                "data", Map.of(
                        "token", token,
                        "user", Map.of(
                                "userId", user.getId(),
                                "email",  user.getEmail(),
                                "name",   user.getName()
                        )
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    /* ---------- 정보 수정 ---------- */
    public ResponseEntity<?> updateUser(String email, UserUpdateRequestDto dto) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "사용자 없음"));
        }

        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (StringUtils.hasText(dto.getEmail())
            && !dto.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(dto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "이미 존재하는 이메일입니다."));
            }
            user.setEmail(dto.getEmail());
        }
        if (StringUtils.hasText(dto.getName())) {
            user.setName(dto.getName());
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "정보 수정 완료"));
    }

    /* ---------- 회원 탈퇴 ---------- */
    public ResponseEntity<?> deleteUser(String email, DeleteRequestDto dto) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "사용자 없음"));
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "비밀번호가 일치하지 않습니다"));
        }

        userRepository.delete(user);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 완료"));
    }
}