package com.example.usersubmanager.mapper;

import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface SubscriptionMapper {

    SubscriptionResponseDto toDto(Subscription subscription);

}
