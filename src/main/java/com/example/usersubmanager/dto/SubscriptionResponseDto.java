package com.example.usersubmanager.dto;

import java.math.BigDecimal;

public record SubscriptionResponseDto(Long id,
                                      String name,
                                      String description,
                                      BigDecimal price) {
}
