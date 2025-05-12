package com.example.usersubmanager.service;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;

public interface UserService {


    UserResponseDto createUser(UserRequestDto user);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto user);

    void deleteById(Long id);
}
