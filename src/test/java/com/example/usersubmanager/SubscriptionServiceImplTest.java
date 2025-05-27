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
        Subscription subscription = subscriptions.stream()
                .filter(s -> s.getId().equals(1L))
                .findFirst()
                .orElseThrow();
        SubscriptionResponseDto subscriptionResponseDto = new SubscriptionResponseDto(1L, "Яндекс подписка", "Базовый доступ к платформе", new BigDecimal("9.99"));

        when(subscriptionRepository.findAllByUsersId(userId)).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any(Subscription.class))).thenReturn(subscriptionResponseDto);
        subscriptionService.getSubscriptionById(userId);

        verify(subscriptionRepository, times(1)).findAllByUsersId(userId);
    }

    @Test
    void getUserByIdNotFound() {
        Long userId = 999L;
        when(subscriptionService.getSubscriptionById(userId)).thenReturn(Collections.emptySet());

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
        Subscription result = subscriptionRepository.findByName(subscriptionRequestDto.name())
                .orElseGet(() -> {
                    Subscription newSub = new Subscription();
                    newSub.setName(subscriptionRequestDto.name());
                    newSub.setDescription(subscriptionRequestDto.description());
                    newSub.setPrice(subscriptionRequestDto.price());
                    return subscriptionRepository.save(newSub);
                });
        assertEquals(subscription.getName(), result.getName());
        assertEquals(subscription.getDescription(), result.getDescription());
        assertEquals(subscription.getPrice(), result.getPrice());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        boolean added = user.getSubscriptions().add(subscription);
        assertTrue(added);

        when(userRepository.save(user)).thenReturn(user);

        subscriptionService.createSubscription(userId, subscriptionRequestDto);

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
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
