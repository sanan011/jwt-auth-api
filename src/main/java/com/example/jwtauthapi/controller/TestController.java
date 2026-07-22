package com.example.jwtauthapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");
        
        return ResponseEntity.ok(Map.of(
                "username", userDetails.getUsername(),
                "role", role
        ));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, String>> getAdminDashboard() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to the admin dashboard!"
        ));
    }
}
