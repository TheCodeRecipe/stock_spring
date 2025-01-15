package com.stockproject.stock_analysis.controller;

import com.stockproject.stock_analysis.entity.UserFavorite;
import com.stockproject.stock_analysis.service.FavoriteService;
import com.stockproject.stock_analysis.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateAccessToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 토큰 검증
            if (!jwtUtil.validateToken(token)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.ok(response);
            }

            // 토큰에서 username 추출
            String username = jwtUtil.extractUsername(token);
            response.put("success", true);
            response.put("username", username);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid token");
            return ResponseEntity.ok(response);
        }
    }

    
}
