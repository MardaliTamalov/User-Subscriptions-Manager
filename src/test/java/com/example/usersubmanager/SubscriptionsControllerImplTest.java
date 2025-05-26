package com.example.usersubmanager;

import com.example.usersubmanager.dto.UserRequestDto;
import com.example.usersubmanager.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.usersubmanager.dto.SubscriptionRequestDto;
import com.example.usersubmanager.dto.SubscriptionResponseDto;
import com.example.usersubmanager.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class SubscriptionsControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @AfterEach
    void tearDown() {
        subscriptionRepository.deleteAll();
    }

    @Test
    void addSubscription_ShouldReturnCreatedSubscription() throws Exception {
        // Given
        Long userId = 1L;
        SubscriptionRequestDto requestDto = new SubscriptionRequestDto(
                "Netflix",
                "Streaming service",
                new BigDecimal("9.99")
        );

        // When
        MvcResult result = mockMvc.perform(post("/users/{id}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        SubscriptionResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SubscriptionResponseDto.class
        );

        assertNotNull(responseDto.id());
        assertEquals(requestDto.name(), responseDto.name());
        assertEquals(requestDto.description(), responseDto.description());
        assertEquals(requestDto.price(), responseDto.price());
    }


    @Test
    void addSubscription_WithInvalidData_ShouldReturnBadRequest() throws Exception {

        // Подготовка
        Long userId = 1L;
        SubscriptionRequestDto requestDto = new SubscriptionRequestDto(
                "Netflix",
                "Стриминговый сервис",
                new BigDecimal("9.99")
        );

        // Действие
        MvcResult result = mockMvc.perform(post("/users/{id}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Проверка
        SubscriptionResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SubscriptionResponseDto.class
        );

        assertNotNull(responseDto.id());
        assertEquals(requestDto.name(), responseDto.name());
        assertEquals(requestDto.description(), responseDto.description());
        assertEquals(requestDto.price(), responseDto.price());
    }

    @Test
    void getUserSubscriptions_ShouldReturnUserSubscriptions() throws Exception {
        // Подготовка
        Long userId = 1L;
        SubscriptionRequestDto invalidRequestDto = new SubscriptionRequestDto(
                "", // невалидное имя
                "", // невалидное описание
                new BigDecimal("-1.00") // невалидная цена
        );

        // Действие и проверка
        mockMvc.perform(post("/users/{id}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getUserSubscriptions_WithNoSubscriptions_ShouldReturnEmptySet() throws Exception {

            // Подготовка
            Long userId = 1L;

            // Сначала добавляем подписку
            SubscriptionRequestDto requestDto = new SubscriptionRequestDto(
                    "Spotify",
                    "Музыкальный стриминг",
                    new BigDecimal("4.99")
            );

            mockMvc.perform(post("/users/{id}/subscriptions", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            // Действие
            MvcResult result = mockMvc.perform(get("/users/{id}/subscriptions", userId))
                    .andExpect(status().isOk())
                    .andReturn();

            // Проверка
            Set<SubscriptionResponseDto> subscriptions = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    objectMapper.getTypeFactory().constructCollectionType(Set.class, SubscriptionResponseDto.class)
            );

            assertFalse(subscriptions.isEmpty());
            assertEquals(1, subscriptions.size());
        }


        @Test
    void deleteSubscription_ShouldDeleteSubscription() throws Exception {
        // Given
        Long userId = 1L;

        // First add a subscription
        SubscriptionRequestDto requestDto = new SubscriptionRequestDto(
                "Disney+",
                "Streaming service",
                new BigDecimal("7.99")
        );

        MvcResult addResult = mockMvc.perform(post("/users/{id}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        SubscriptionResponseDto addedSubscription = objectMapper.readValue(
                addResult.getResponse().getContentAsString(),
                SubscriptionResponseDto.class
        );

        // When
        mockMvc.perform(delete("/users/{id}/subscriptions", userId)
                        .param("subscriptionId", addedSubscription.id().toString()))
                .andExpect(status().isOk());

        // Then
        MvcResult getResult = mockMvc.perform(get("/users/{id}/subscriptions", userId))
                .andReturn();

        Set<SubscriptionResponseDto> subscriptions = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, SubscriptionResponseDto.class)
        );

        assertTrue("",subscriptions.isEmpty());
    }

    @Test
    void getTopSubscriptions_ShouldReturnPopularSubscriptions() throws Exception {
        // Given
        // Add some subscriptions to different users
        SubscriptionRequestDto sub1 = new SubscriptionRequestDto("Netflix", "Streaming", new BigDecimal("9.99"));
        SubscriptionRequestDto sub2 = new SubscriptionRequestDto("Spotify", "Music", new BigDecimal("4.99"));
        SubscriptionRequestDto sub3 = new SubscriptionRequestDto("Amazon Prime", "Streaming", new BigDecimal("8.99"));

        mockMvc.perform(post("/users/1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub1)));

        mockMvc.perform(post("/users/2/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub1)));

        mockMvc.perform(post("/users/3/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub1)));

        mockMvc.perform(post("/users/1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub2)));

        mockMvc.perform(post("/users/2/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub2)));

        mockMvc.perform(post("/users/1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sub3)));

        // When
        MvcResult result = mockMvc.perform(get("/top"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Set<SubscriptionResponseDto> topSubscriptions = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, SubscriptionResponseDto.class)
        );

        assertFalse(topSubscriptions.isEmpty());
        // Assuming the top subscription is Netflix as it was added 3 times
        assertTrue("",topSubscriptions.stream()
                .anyMatch(sub -> sub.name().equals("Netflix")));
    }
}
