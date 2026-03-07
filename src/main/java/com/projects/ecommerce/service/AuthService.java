package com.projects.ecommerce.service;

import com.projects.ecommerce.exception.BadRequestException;
import com.projects.ecommerce.exception.ResourceNotFoundException;
import com.projects.ecommerce.model.dto.Auth.AuthResponseDTO;
import com.projects.ecommerce.model.dto.Auth.LoginRequestDTO;
import com.projects.ecommerce.model.dto.Auth.RegisterRequestDTO;
import com.projects.ecommerce.model.entity.User.Role;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.repository.UserRepository;
import com.projects.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {

        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {

            throw new BadRequestException("User with email: " + registerRequestDTO.getEmail() + " already exists");

        }

        User user = User.builder()
                .name(registerRequestDTO.getName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return new AuthResponseDTO(jwtUtil.generateToken(user));

    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new AuthResponseDTO(jwtUtil.generateToken(user));

    }

}
