package com.example.usersubmanager.mapper;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.dto.UserResponseDto;
import com.example.usersubmanager.entity.Subscription;
import com.example.usersubmanager.entity.User;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring"
)

public interface UserMapper {


  //  @Mapping(target = "subscriptions", source = "subscriptionIds", qualifiedByName = "mapIdsToSubscriptions")
    User requestToUser(UserRequestDto userRequestDto);

    UserResponseDto userToUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);

    @Named("mapIdsToSubscriptions")
    static Set<Subscription> mapIdsToSubscriptions(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        Set<Subscription> subscriptions = new HashSet<>();
        for (Long id : ids) {
            Subscription s = new Subscription();
            s.setId(id);
            subscriptions.add(s);
        }
        return subscriptions;
    }
}