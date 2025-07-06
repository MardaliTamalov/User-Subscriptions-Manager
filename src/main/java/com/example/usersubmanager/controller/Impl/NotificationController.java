package com.example.usersubmanager.controller.Impl;

import com.example.usersubmanager.dto.NotificationDto;
import com.example.usersubmanager.kafka.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationProducer producer;

    @PostMapping("/kafka")
    public ResponseEntity sendMessage(@RequestBody NotificationDto notificationDto) {
        producer.sendMessage(notificationDto);
        return ResponseEntity.ok().build();
    }

}
