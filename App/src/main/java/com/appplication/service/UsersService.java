package com.appplication.service;

import java.util.List;

import com.appplication.dto.UsersRequestDto;
import com.appplication.dto.UsersResponseDto;
import com.appplication.dto.UsersUpdateRequestDto;

 
public interface UsersService {

    String register(UsersRequestDto user);

    UsersResponseDto searchUser(Long id);

    List<UsersResponseDto> viewAllUsers();

    UsersResponseDto updateUser(UsersUpdateRequestDto userDto);

    String deleteUser(Long id);
}