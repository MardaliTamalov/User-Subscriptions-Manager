package com.example.usersubmanager.controller.Impl;

import com.example.usersubmanager.controller.UsersController;
import com.example.usersubmanager.dto.UserResponseDto;
import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UsersControllerImpl implements UsersController {
    private final UserService userService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addUsers(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "users", key = "#id")
    public UserResponseDto getUser(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(5000);
        return userService.getUserById(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @CachePut(value = "users", key = "#id")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
