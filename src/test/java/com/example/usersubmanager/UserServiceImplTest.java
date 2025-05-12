package com.example.usersubmanager;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import com.example.usersubmanager.entity.User;
import com.example.usersubmanager.exception.UserNotFoundException;
import com.example.usersubmanager.mapper.UserMapper;
import com.example.usersubmanager.repository.UserRepository;
import com.example.usersubmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserSuccess() {
        UserRequestDto requestDto = new UserRequestDto("John", "john@example.com");
        User user = new User(1L, "John", "john@example.com", new HashSet<>());
        UserResponseDto responseDto = new UserResponseDto(1L, "John", "john@example.com");

        when(userMapper.requestToUser(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertEquals(responseDto, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByIdSuccess() {
        Long userId = 1L;
        User user = new User(userId, "John", "john@example.com", new HashSet<>());
        UserResponseDto responseDto = new UserResponseDto(userId, "John", "john@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUserById(userId);

        assertEquals(responseDto, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateUserSuccess() {
        Long userId = 1L;
        UserRequestDto requestDto = new UserRequestDto("John Updated", "john.updated@example.com");
        User existingUser = new User(userId, "John", "john@example.com", new HashSet<>());
        User updatedUser = new User(userId, "John Updated", "john.updated@example.com", new HashSet<>());
        UserResponseDto responseDto = new UserResponseDto(userId, "John Updated", "john.updated@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.userToUserResponseDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertEquals("John Updated", result.name());
        assertEquals("john.updated@example.com", result.email());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteByIdSuccess() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteByIdNotFound() {
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
    }
}