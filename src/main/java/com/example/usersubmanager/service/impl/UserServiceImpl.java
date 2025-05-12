package com.example.usersubmanager.service.impl;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import com.example.usersubmanager.entity.User;
import com.example.usersubmanager.exception.UserNotFoundException;
import com.example.usersubmanager.mapper.UserMapper;
import com.example.usersubmanager.repository.UserRepository;
import com.example.usersubmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        log.info("Creating new user");
        return userMapper.userToUserResponseDto(userRepository.save(userMapper.requestToUser(user)));
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Обновляем поля с помощью маппера
        userMapper.updateUserFromDto(userRequestDto, existingUser);

        // Сохраняем изменения
        User savedUser = userRepository.save(existingUser);

        // Преобразуем в DTO и возвращаем
        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting user with id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else throw new UserNotFoundException("User not found with id: " + id);
    }
}
