package com.appplication.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appplication.dto.UsersRequestDto;
import com.appplication.dto.UsersResponseDto;
import com.appplication.dto.UsersUpdateRequestDto;
import com.appplication.entity.Users;
import com.appplication.repository.UsersRepository;

@Service
@Transactional
public class UserServiceImplementation implements UsersService, UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UsersRepository usersRepository,
                                     PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Spring Security UserDetailsService implementation ---
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String role = user.getRole();
        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        } else if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    // --- Business Methods ---
    @Override
    public String register(UsersRequestDto request) {
        validateUserRequest(request);

        if (usersRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()
                && usersRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (request.getMobile() != null && !request.getMobile().isBlank()
                && usersRepository.existsByMobile(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number already exists");
        }

        Users user = new Users();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setDpUrl(request.getDpUrl());

        usersRepository.save(user);
        return "User registered successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public UsersResponseDto searchUser(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        return mapToResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsersResponseDto> viewAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public UsersResponseDto updateUser(UsersUpdateRequestDto request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        Users user = usersRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getId()));

        if (request.getUsername() != null && !request.getUsername().isBlank()
                && !request.getUsername().equals(user.getUsername())) {
            if (usersRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equals(user.getEmail())) {
            if (usersRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getMobile() != null && !request.getMobile().isBlank()
                && !request.getMobile().equals(user.getMobile())) {
            if (usersRepository.existsByMobile(request.getMobile())) {
                throw new IllegalArgumentException("Mobile number already exists");
            }
            user.setMobile(request.getMobile());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getDob() != null) user.setDob(request.getDob());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getDpUrl() != null) user.setDpUrl(request.getDpUrl());

        return mapToResponseDto(usersRepository.save(user));
    }

    @Override
    public String deleteUser(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        usersRepository.delete(user);
        return "User deleted successfully";
    }

    private void validateUserRequest(UsersRequestDto request) {
        if (request == null) throw new IllegalArgumentException("User request is required");
        if (request.getUsername() == null || request.getUsername().isBlank()) throw new IllegalArgumentException("Username is required");
        if (request.getPassword() == null || request.getPassword().isBlank()) throw new IllegalArgumentException("Password is required");
    }

    private UsersResponseDto mapToResponseDto(Users user) {
        UsersResponseDto response = new UsersResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setMobile(user.getMobile());
        response.setDob(user.getDob());
        response.setGender(user.getGender());
        response.setAddress(user.getAddress());
        response.setDpUrl(user.getDpUrl());
        return response;
    }
}
