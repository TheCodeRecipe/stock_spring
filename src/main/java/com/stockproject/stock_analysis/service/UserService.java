package com.stockproject.stock_analysis.service;

import com.stockproject.stock_analysis.entity.User;
import com.stockproject.stock_analysis.repository.UserRepository;
import com.stockproject.stock_analysis.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입 처리
    public Map<String, Object> registerUser(User user) {
        Map<String, Object> response = new HashMap<>();

        // 중복 검사
        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("success", false);
            response.put("message", "이미 존재하는 사용자 이름입니다.");
            return response;
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("success", false);
            response.put("message", "이미 존재하는 이메일입니다.");
            return response;
        }

        // 비밀번호 암호화 및 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "회원가입이 완료되었습니다.");
        return response;
    }

    // 로그인 처리 메서드
    public Map<String, Object> loginUser(String username, String password) {
        Map<String, Object> response = new HashMap<>();

        // 사용자 존재 여부 확인
        Optional<User> userOptional = userRepository.findByUsername(username);
        System.out.println("userOptional: " + userOptional);
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "존재하지 않는 사용자입니다.");
            return response;
        }

        // Optional에서 User 객체를 꺼냄
        User user = userOptional.get();
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return response;
        }
        
        // JWT 생성
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // role 정보 추가
        String role = user.getRole(); // User 객체에서 역할 정보를 가져옴
        
        response.put("success", true);
        response.put("message", "로그인에 성공했습니다.");
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("role", role);
        
        return response;
    }

    
    // 사용자 검색 by username
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 사용자 검색 by email
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
