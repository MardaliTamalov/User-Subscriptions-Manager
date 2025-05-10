package com.example.usersubmanager.service.impl;

import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.entity.Subscription;
import com.example.usersubmanager.entity.User;
import com.example.usersubmanager.exception.SubscriptionNotFoundException;
import com.example.usersubmanager.exception.UserNotFoundException;
import com.example.usersubmanager.mapper.SubscriptionMapper;
import com.example.usersubmanager.repository.SubscriptionRepository;
import com.example.usersubmanager.repository.UserRepository;
import com.example.usersubmanager.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;

    @Override
    public Set<SubscriptionResponseDto> getSubscriptionById(Long userId) {
        log.info("Fetching subscriptions for user with id: {}", userId);

        Set<Subscription> subscriptions = subscriptionRepository.findAllByUsersId(userId);

        if (subscriptions.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return subscriptions.stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public SubscriptionResponseDto createSubscription(Long userId, SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Creating subscription for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Проверка, существует ли уже такая подписка (по имени)
        Subscription subscription = subscriptionRepository.findByName(subscriptionRequestDto.name())
                .orElseGet(() -> {
                    Subscription newSub = new Subscription();
                    newSub.setName(subscriptionRequestDto.name());
                    newSub.setDescription(subscriptionRequestDto.description());
                    newSub.setPrice(subscriptionRequestDto.price());
                    return subscriptionRepository.save(newSub);
                });

        // Добавляем подписку пользователю
        user.getSubscriptions().add(subscription);
        userRepository.save(user); // сохраняем связь в таблице user_subscriptions

        return new SubscriptionResponseDto(
                subscription.getId(),
                subscription.getName(),
                subscription.getDescription(),
                subscription.getPrice()
        );
    }

    @Override
    public void deleteSubscriptionById(Long id) {
        log.info("Deleting subscription with id: {}", id);
        if (subscriptionRepository.existsById(id)) {
            subscriptionRepository.deleteById(id);
        } else throw new SubscriptionNotFoundException("Subscription not found with id: " + id);
    }

    @Override
    public Set<SubscriptionResponseDto> getTopSubscriptions() {
        return subscriptionRepository.findTop3PopularSubscriptions().stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toSet());
    }
}
