package com.appplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.appplication.dto.UsersRequestDto;
import com.appplication.dto.UsersResponseDto;
import com.appplication.dto.UsersUpdateRequestDto;
import com.appplication.entity.Users;
import com.appplication.repository.UsersRepository;

 

@Service
public class UserServiceImplementation implements UsersService {
     
    //private final Key SIGNING_KEY;
    private final UsersRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UsersRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(UsersRequestDto userDto) {

        if (repo.existsByUsername(userDto.getUsername())) {
            return "Username already exists";
        }

        if (repo.existsByEmail(userDto.getEmail())) {
            return "Email already exists";
        }

        if (repo.existsByMobile(userDto.getMobile())) {
            return "Mobile number already exists";
        }

        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setMobile(userDto.getMobile());
        user.setDob(userDto.getDob());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        user.setDpUrl(userDto.getDpUrl());

        repo.save(user);

        return "User registered successfully";
    }

    @Override
    public UsersResponseDto searchUser(Long id) {
        Optional<Users> optionalUser = repo.findById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();
        return mapToResponseDto(user);
    }

    @Override
    public List<UsersResponseDto> viewAllUsers() {
        List<Users> users = repo.findAll();

        return users.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public UsersResponseDto updateUser(UsersUpdateRequestDto userDto) {

        if (userDto.getId() == null) {
            throw new RuntimeException("User id is required");
        }

        Optional<Users> optionalUser = repo.findById(userDto.getId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users existingUser = optionalUser.get();

        if (!existingUser.getUsername().equals(userDto.getUsername())
                && repo.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (!existingUser.getEmail().equals(userDto.getEmail())
                && repo.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!existingUser.getMobile().equals(userDto.getMobile())
                && repo.existsByMobile(userDto.getMobile())) {
            throw new RuntimeException("Mobile number already exists");
        }

        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setMobile(userDto.getMobile());
        existingUser.setDob(userDto.getDob());
        existingUser.setGender(userDto.getGender());
        existingUser.setAddress(userDto.getAddress());
        existingUser.setDpUrl(userDto.getDpUrl());

        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        Users updatedUser = repo.save(existingUser);

        return mapToResponseDto(updatedUser);
    }

    @Override
    public String deleteUser(Long id) {
        if (!repo.existsById(id)) {
            return "User not found";
        }

        repo.deleteById(id);
        return "User deleted successfully";
    }

    private UsersResponseDto mapToResponseDto(Users user) {
        UsersResponseDto dto = new UsersResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setDpUrl(user.getDpUrl());
        return dto;
    }
}
