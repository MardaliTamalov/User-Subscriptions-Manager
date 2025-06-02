package com.example.usersubmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UserResponseDto(
        @Schema(description = "уникальное идентификатор пользователя", example = "1") Long id,
        @Schema(description = "имя пользователя", example = "Иван")String name,
        @Schema(description = "почта пользователя", example = "name@mail.ru")String email)
        //   Set<SubscriptionResponseDto> subscriptions)
{
}
