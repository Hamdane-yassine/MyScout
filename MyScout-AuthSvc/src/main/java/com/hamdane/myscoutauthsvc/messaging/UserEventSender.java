package com.hamdane.myscoutauthsvc.messaging;

import com.hamdane.myscoutauthsvc.dto.UserEventPayload;
import com.hamdane.myscoutauthsvc.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventSender {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, UserEventPayload> kafkaTemplate;

    public void sendUserCreated(User user) {
        log.info("sending user created event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.CREATED));
    }

    public void sendUserUpdated(User user) {
        log.info("sending user updated event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.UPDATED));
    }

    public void sendUserUpdated(User user, String oldPicUrl) {
        log.info("sending user updated (profile pic changed) event for user {}",
                user.getUsername());

        UserEventPayload payload = convertTo(user, UserEventType.UPDATED);
        payload.setOldProfilePicUrl(oldPicUrl);

        sendUserChangedEvent(payload);
    }

    private void sendUserChangedEvent(UserEventPayload payload) {

        kafkaTemplate.send(topicName,payload);

        log.info("user event {} sent to topic {} for user {}",
                payload.getEventType().name(),
                "producer-out-0",
                payload.getUsername());
    }

    private UserEventPayload convertTo(User user, UserEventType eventType) {
        UserEventPayload payload = new UserEventPayload();
        payload.setEventType(eventType);
        payload.setId(user.getId());
        payload.setUsername(user.getUsername());
        payload.setEmail(user.getEmail());
        payload.setDisplayName(user.getUserProfile().getDisplayName());
        payload.setProfilePictureUrl(user.getUserProfile().getProfilePictureUrl());
        return payload;
    }
}
