package com.example.usersubmanager.controller.Impl;

import com.example.usersubmanager.controller.SubscriptionsController;
import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping()
@RequiredArgsConstructor

public class SubscriptionsControllerImpl implements SubscriptionsController {
    private final SubscriptionService subscriptionService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto addSubscription(@PathVariable Long user_id, @Valid @RequestBody  SubscriptionRequestDto subscriptionRequestDto) {
        return subscriptionService.createSubscription(user_id,subscriptionRequestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Set<SubscriptionResponseDto> getUserSubscriptions(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscriptionById(id);

    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Set<SubscriptionResponseDto> getTopSubscriptions() {
        return subscriptionService.getTopSubscriptions();
    }
}
