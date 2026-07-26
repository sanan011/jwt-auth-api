package com.example.jwtauthapi.controller;

import com.example.jwtauthapi.dto.AuthResponse;
import com.example.jwtauthapi.dto.LoginRequest;
import com.example.jwtauthapi.dto.RegisterRequest;
import com.example.jwtauthapi.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        /*
         * CLIENT USAGE NOTE:
         * A client would typically decode the JWT payload to check its expiration time.
         * Shortly before the token expires (e.g., 5 minutes before), the client calls
         * this /api/auth/refresh endpoint with the current valid token to get a new one.
         * This silently keeps the user session active without requiring a full re-login.
         */
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        try {
            AuthResponse newAuthResponse = authService.refresh(token);
            return ResponseEntity.ok(newAuthResponse);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Token has already expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid token"));
        }
    }
}
