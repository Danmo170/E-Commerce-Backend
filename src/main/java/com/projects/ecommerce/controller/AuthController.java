package com.projects.ecommerce.controller;

import com.projects.ecommerce.model.dto.AuthResponseDTO;
import com.projects.ecommerce.model.dto.LoginRequestDTO;
import com.projects.ecommerce.model.dto.RegisterRequestDTO;
import com.projects.ecommerce.model.entity.Role;
import com.projects.ecommerce.model.entity.User;
import com.projects.ecommerce.repository.UserRepository;
import com.projects.ecommerce.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {

            throw new RuntimeException("User with email: " + registerRequestDTO.getEmail() + " already exists");

        }

        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return new AuthResponseDTO(jwtUtil.generateToken(user));

    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponseDTO(jwtUtil.generateToken(user));

    }

}
