package com.stockproject.stock_analysis.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.stockproject.stock_analysis.entity.User;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
	private final Key SIGNING_KEY;
    
	// Application.properties에서 키를 읽어옴
    public JwtUtil(@Value("${secret.key}") String secretKey) {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.SIGNING_KEY = Keys.hmacShaKeyFor(keyBytes); // Keys 클래스를 사용하여 키 생성
    }
	
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 검증 실패
        }
    }

    // 토큰에서 username 추출
    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.get("username", String.class);
    }
    

 // JwtUtil.java
    public String extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class); // role 정보 추출
    }
    
    public String generateAccessToken(User user) {
        return Jwts.builder()
        	.claim("username", user.getUsername()) // 유저네임 포함	
            .claim("role", user.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
        	.claim("username", user.getUsername()) // 유저네임 포함	
            .claim("role", user.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    // 인증 객체 생성
    public Authentication getAuthentication(String username, String role) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
