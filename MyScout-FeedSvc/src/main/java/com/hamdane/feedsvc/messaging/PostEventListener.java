package com.hamdane.feedsvc.messaging;

import com.hamdane.feedsvc.model.Post;
import com.hamdane.feedsvc.payload.PostEventPayload;
import com.hamdane.feedsvc.service.FeedGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final FeedGeneratorService feedGeneratorService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "post", containerFactory = "factory")
    public void consume(PostEventPayload message) {
        PostEventType eventType = message.getEventType();
        switch (eventType) {
            case CREATED:
                feedGeneratorService.addToFeed(convertTo(message));
                break;
            case DELETED:
                break;
        }
    }

    private Post convertTo(PostEventPayload payload) {
        return Post.builder().id(payload.getId()).createdAt(payload.getCreatedAt()).username(payload.getUsername()).build();
    }
}
