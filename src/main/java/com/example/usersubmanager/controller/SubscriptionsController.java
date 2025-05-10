package com.example.usersubmanager.controller;

import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

public interface SubscriptionsController {

    @PostMapping("/users/{id}/subscriptions")
    public SubscriptionResponseDto addSubscription(@PathVariable Long id, @RequestBody SubscriptionRequestDto subscriptionRequestDto);

    @GetMapping("/users/{id}/subscriptions")
    public Set<SubscriptionResponseDto> getUserSubscriptions(@PathVariable Long id);

    @DeleteMapping("/users/{id}/subscriptions")
    public void deleteSubscription(@PathVariable Long id);

    @GetMapping("/top")
    public Set<SubscriptionResponseDto> getTopSubscriptions();
}
