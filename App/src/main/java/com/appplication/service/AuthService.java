package com.appplication.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appplication.dto.AuthResponse;
import com.appplication.dto.LoginRequest;
import com.appplication.dto.RegisterRequest;
import com.appplication.entity.Users;
import com.appplication.repository.UsersRepository;

@Service
@Transactional
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request == null
                || request.name() == null
                || request.name().isBlank()
                || request.username() == null
                || request.username().isBlank()
                || request.password() == null
                || request.password().isBlank()) {
            throw new IllegalArgumentException(
                    "Name, username, and password are required");
        }

        if (usersRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException(
                    "Username already exists");
        }

        Users user = new Users(
                null, request.name(),
                request.username(),
                passwordEncoder.encode(request.password()),
                "USER", null, null, null, null, null, null);

        Users savedUser = usersRepository.save(user);
        String token = jwtService.generateToken(savedUser.getUsername());

        return new AuthResponse(
                token,
                "Bearer",
                savedUser.getUsername(),
                savedUser.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        if (request == null
                || request.username() == null
                || request.password() == null) {
            throw new BadCredentialsException(
                    "Invalid username or password");
        }

        Users user = usersRepository.findByUsername(request.username())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid username or password"));

        if (!passwordEncoder.matches(
                request.password(), user.getPassword())) {
            throw new BadCredentialsException(
                    "Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getRole());
    }
}