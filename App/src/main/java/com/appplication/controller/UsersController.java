package com.appplication.controller;
 

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appplication.dto.UsersRequestDto;
import com.appplication.dto.UsersResponseDto;
import com.appplication.dto.UsersUpdateRequestDto;
import com.appplication.service.UsersService;

 
@RestController
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        super();
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestBody UsersRequestDto user) {
        return service.register(user);
    }

    @GetMapping("/searchUser/{id}")
    public UsersResponseDto searchUser(@PathVariable Long id) {
        return service.searchUser(id);
    }

    @GetMapping("/viewAllUsers")
    public List<UsersResponseDto> viewAllUsers() {
        return service.viewAllUsers();
    }

    @PutMapping("/updateUser")
    public UsersResponseDto updateUser(@RequestBody UsersUpdateRequestDto userDto) {
        return service.updateUser(userDto);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }
}
