package com.example.university.security.controller;

import com.example.university.role.model.request.LoginRequest;
import com.example.university.security.constants.SecurityConstants;
import com.example.university.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, SecurityConstants.AUTH_HEADER + token)
                .build();
    }
}
