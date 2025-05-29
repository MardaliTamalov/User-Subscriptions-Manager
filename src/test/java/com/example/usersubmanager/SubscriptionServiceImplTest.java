package com.example.usersubmanager;

import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.entity.Subscription;
import com.example.usersubmanager.entity.User;
import com.example.usersubmanager.exception.SubscriptionNotFoundException;
import com.example.usersubmanager.exception.UserNotFoundException;
import com.example.usersubmanager.mapper.SubscriptionMapper;
import com.example.usersubmanager.repository.SubscriptionRepository;
import com.example.usersubmanager.repository.UserRepository;
import com.example.usersubmanager.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    SubscriptionRepository subscriptionRepository;

    @Mock
    SubscriptionMapper subscriptionMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    @Test
    void getSubscriptionByIdSuccess() {
        Long userId = 1L;
        Set<Subscription> subscriptions = Set.of(
                new Subscription(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"), new HashSet<>()),
                new Subscription(2L, "VK музыка", "Полный доступ, включая премиум функции", new BigDecimal("19.99"), new HashSet<>())
        );
        Subscription subscription1 = subscriptions.stream()
                .filter(s -> s.getId().equals(1L))
                .findFirst()
                .orElseThrow();
        Subscription subscription2 = subscriptions.stream()
                .filter(s -> s.getId().equals(2L))
                .findFirst()
                .orElseThrow();
        SubscriptionResponseDto subscriptionResponseDto1 = new SubscriptionResponseDto(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));
        SubscriptionResponseDto subscriptionResponseDto2 = new SubscriptionResponseDto(2L, "VK музыка", "Полный доступ, включая премиум функции", new BigDecimal("19.99"));
        when(subscriptionRepository.findAllByUsersId(userId)).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(subscription1)).thenReturn(subscriptionResponseDto1);
        when(subscriptionMapper.toDto(subscription2)).thenReturn(subscriptionResponseDto2);
        subscriptionService.getSubscriptionById(userId);

        verify(subscriptionRepository, times(1)).findAllByUsersId(userId);
    }

    @Test
    void getUserByIdNotFound() {
        Long userId = 999L;
        when(subscriptionRepository.findAllByUsersId(userId)).thenReturn(Collections.emptySet());

        assertThrows(UserNotFoundException.class, () -> subscriptionService.getSubscriptionById(userId));

        verify(subscriptionRepository, times(1)).findAllByUsersId(userId);
    }

    @Test
    void createSubscriptionSuccess() {
        Long userId = 1L;
        Subscription subscription = new Subscription(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"), new HashSet<>());
        User user = new User(userId, "John", "john@example.com", new HashSet<>());
        SubscriptionRequestDto subscriptionRequestDto = new SubscriptionRequestDto("Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));

        when(subscriptionRepository.findByName(subscriptionRequestDto.name())).thenReturn(Optional.of(subscription));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        SubscriptionResponseDto subscriptionResponseDto = subscriptionService.createSubscription(userId, subscriptionRequestDto);
        assertEquals(subscriptionRequestDto.name(), subscriptionResponseDto.name());
        assertEquals(subscriptionRequestDto.description(), subscriptionResponseDto.description());
        assertEquals(subscriptionRequestDto.price(), subscriptionResponseDto.price());

        verify(subscriptionRepository, times(0)).save(any(Subscription.class));
        verify(userRepository, times(1)).findById(userId);
        verify(subscriptionRepository, times(1)).findByName(subscriptionRequestDto.name());
    }


    @Test
    void createNewSubscriptionSuccess() {
        Long userId = 1L;
        Subscription subscription = new Subscription(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"), new HashSet<>());
        User user = new User(userId, "John", "john@example.com", new HashSet<>());
        SubscriptionRequestDto subscriptionRequestDto = new SubscriptionRequestDto("Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));

        when(subscriptionRepository.findByName(subscriptionRequestDto.name())).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        SubscriptionResponseDto subscriptionResponseDto = subscriptionService.createSubscription(userId, subscriptionRequestDto);
        assertEquals(subscriptionRequestDto.name(), subscriptionResponseDto.name());
        assertEquals(subscriptionRequestDto.description(), subscriptionResponseDto.description());
        assertEquals(subscriptionRequestDto.price(), subscriptionResponseDto.price());

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(userRepository, times(1)).findById(userId);
        verify(subscriptionRepository, times(1)).findByName(subscriptionRequestDto.name());
    }

    @Test
    void creatSubscriptionWhenUserNotFound() {
        Long userId = 999L;
        SubscriptionRequestDto subscriptionRequestDto = new SubscriptionRequestDto("Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> subscriptionService.createSubscription(userId, subscriptionRequestDto));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteSubscriptionByIdSuccess() {
        Long userId = 1L;
        when(subscriptionRepository.existsById(userId)).thenReturn(true);
        subscriptionService.deleteSubscriptionById(userId);

        verify(subscriptionRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteSubscriptionByIdNotFound() {
        Long subscriptionId = 999L;
        when(subscriptionRepository.existsById(subscriptionId)).thenReturn(false);
        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.deleteSubscriptionById(subscriptionId));

        verify(subscriptionRepository, times(1)).existsById(subscriptionId);
    }

    @Test
    void getTopSubscriptionsSuccess() {
        Set<Subscription> subscriptions = Set.of(
                new Subscription(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"), new HashSet<>()),
                new Subscription(2L, "VK музыка", "Полный доступ, включая премиум функции", new BigDecimal("19.99"), new HashSet<>())
        );
        SubscriptionResponseDto subscriptionResponseDto = new SubscriptionResponseDto(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));

        when(subscriptionRepository.findTop3PopularSubscriptions()).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any(Subscription.class))).thenReturn(subscriptionResponseDto);

        subscriptionService.getTopSubscriptions();

        verify(subscriptionRepository, times(1)).findTop3PopularSubscriptions();
    }
}
