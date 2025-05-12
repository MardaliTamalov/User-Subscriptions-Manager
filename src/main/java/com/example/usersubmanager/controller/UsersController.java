package com.example.usersubmanager.controller;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import org.springframework.web.bind.annotation.*;

public interface UsersController {

    @PostMapping()
    public UserResponseDto addUsers(@RequestBody UserRequestDto userRequestDto);

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id);

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto);

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id);
}
