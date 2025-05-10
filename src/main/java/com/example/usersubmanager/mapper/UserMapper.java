package com.example.usersubmanager.mapper;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import com.example.usersubmanager.entity.User;
import org.mapstruct.*;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring"
)

public interface UserMapper {

    User requestToUser(UserRequestDto user);

    UserResponseDto userToUserResponseDto(User user);

    User userDtoResponseToUser(UserResponseDto userDto);
}
