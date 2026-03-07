package com.projects.ecommerce.controller;

import com.projects.ecommerce.model.dto.Auth.AuthResponseDTO;
import com.projects.ecommerce.model.dto.Auth.LoginRequestDTO;
import com.projects.ecommerce.model.dto.Auth.RegisterRequestDTO;
import com.projects.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        return ResponseEntity.ok(authService.register(registerRequestDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        return ResponseEntity.ok(authService.login(loginRequestDTO));

    }

}
