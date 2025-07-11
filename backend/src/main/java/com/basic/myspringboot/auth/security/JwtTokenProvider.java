package com.basic.myspringboot.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    /** application.properties(.env) 에 Base64 문자열로 정의한 비밀키 */
    @Value("${jwt.secret}")
    private String secretKeyBase64;

    /** 토큰 유효 기간(1일) */
    private final long validityMs = 1_000L * 60 * 60 * 24;

    /** ------------ 내부 유틸 ------------- */

    /** Base64 → byte[] → Key 객체 (길이 검증 포함) */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        return Keys.hmacShaKeyFor(keyBytes);   // 256bit 이상 아니면 IllegalArgumentException 발생
    }

    /** ------------ 토큰 생성 ------------- */
    public String createToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(exp)
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    /** ------------ 토큰 검증 및 파싱 ------------- */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);    // 서명·만료 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
}