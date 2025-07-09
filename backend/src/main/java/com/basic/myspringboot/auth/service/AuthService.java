package com.basic.myspringboot.auth.service;

import com.basic.myspringboot.auth.dto.*;
import com.basic.myspringboot.auth.entity.User;
import com.basic.myspringboot.auth.repository.UserRepository;
import com.basic.myspringboot.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final JwtTokenProvider    jwtTokenProvider;

    /* ---------- 회원가입 ---------- */
    public ResponseEntity<String> signup(SignupRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("이미 존재하는 username입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    /* ---------- 로그인 ---------- */
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String token = jwtTokenProvider.createToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(token, user.getUsername()));
    }

    /* ---------- 정보 수정 ---------- */
    public ResponseEntity<String> updateUser(String username, UserUpdateRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }

        userRepository.save(user);
        return ResponseEntity.ok("정보 수정 완료");
    }

    /* ---------- 회원 탈퇴 ---------- */
    public ResponseEntity<String> deleteUser(String username, DeleteRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}