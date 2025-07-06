package com.example.usersubmanager.kafka;

import com.example.usersubmanager.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.util.MimeTypeUtils;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final StreamBridge streamBridge;
    private static final String BINDING_NAME= "notifications-producer-out-0";

    public void sendMessage(NotificationDto notificationDto){
        Message<NotificationDto> message= MessageBuilder.withPayload(notificationDto).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        streamBridge.send(BINDING_NAME, message);
    }
}
