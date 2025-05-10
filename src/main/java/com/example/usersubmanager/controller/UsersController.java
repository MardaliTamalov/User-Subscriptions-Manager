package com.example.usersubmanager.controller;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import org.springframework.web.bind.annotation.*;

public interface UsersController {

    @PostMapping()
    public UserResponseDto addUsers(@RequestBody UserRequestDto userRequestDto);

    @GetMapping("/{id}")
    //@Operation(summary = "Get user by ID")
    public UserResponseDto getUser(@PathVariable Long id);


    @PutMapping
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto);

    @DeleteMapping
    public void deleteUser(@RequestParam Long id);

}
