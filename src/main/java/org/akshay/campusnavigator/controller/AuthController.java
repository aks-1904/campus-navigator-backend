package org.akshay.campusnavigator.controller;

import org.akshay.campusnavigator.dto.ResponseDTOs;
import org.akshay.campusnavigator.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(
                    ResponseDTOs.ApiResponse.ok("Login successful", Map.of("token", token))
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTOs.ApiResponse.error("Invalid credentials"));
    }
}