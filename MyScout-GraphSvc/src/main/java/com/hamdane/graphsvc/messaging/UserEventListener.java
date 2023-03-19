package com.hamdane.graphsvc.messaging;

import com.hamdane.graphsvc.model.User;
import com.hamdane.graphsvc.payload.UserEventPayload;
import com.hamdane.graphsvc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "user",containerFactory = "factory")
    public void consume(UserEventPayload message) {
        UserEventType eventType = message.getEventType();
        User user = convertTo(message);
        switch (eventType) {
            case CREATED -> userService.addUser(user);
            case UPDATED -> userService.updateUser(user);
        }
    }

    private User convertTo(UserEventPayload payload) {
        return User.builder().userId(payload.getId()).username(payload.getUsername()).name(payload.getDisplayName()).profilePic(payload.getProfilePictureUrl()).build();
    }
}
