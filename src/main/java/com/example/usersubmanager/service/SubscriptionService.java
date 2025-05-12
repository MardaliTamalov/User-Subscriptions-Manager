package com.example.usersubmanager.service;

import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.entity.Subscription;

import java.util.Set;

public interface SubscriptionService {

    Set<SubscriptionResponseDto> getSubscriptionById(Long userId);

    SubscriptionResponseDto createSubscription(Long userId, SubscriptionRequestDto subscriptionRequestDto);

    void deleteSubscriptionById(Long id);

    Set<SubscriptionResponseDto> getTopSubscriptions();

}
