package com.example.usersubmanager.controller;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Пример api", description = "пример controllera")
public interface UsersController {

    @PostMapping()
    public UserResponseDto addUsers(@RequestBody UserRequestDto userRequestDto);

    @Operation(summary = "получение пользователя по id", description = " обращение у бд для получения пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно получен пользователь"),
            @ApiResponse(responseCode = "404",description = "not found")
    })
    @Parameter(name = "id", description = "id пользователя", example = "1")
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) throws InterruptedException;

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto);

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id);
}
