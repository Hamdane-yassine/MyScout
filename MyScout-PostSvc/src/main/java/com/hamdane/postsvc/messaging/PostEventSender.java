package com.hamdane.postsvc.messaging;


import com.hamdane.postsvc.dto.PostEventPayload;
import com.hamdane.postsvc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventSender {

    private final KafkaTemplate<String, PostEventPayload> kafkaTemplate;
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public void sendPostCreated(Post post) {
        sendPostChangedEvent(convertTo(post, PostEventType.CREATED));
    }

    public void sendPostUpdated(Post post) {
        sendPostChangedEvent(convertTo(post, PostEventType.UPDATED));
    }

    public void sendPostDeleted(Post post) {
        sendPostChangedEvent(convertTo(post, PostEventType.DELETED));
    }

    private void sendPostChangedEvent(PostEventPayload payload) {
        kafkaTemplate.send(topicName, payload);
    }

    private PostEventPayload convertTo(Post post, PostEventType eventType) {
        PostEventPayload payload = new PostEventPayload();
        payload.setEventType(eventType);
        payload.setId(post.getId());
        payload.setId(post.getId());
        payload.setUsername(post.getUsername());
        payload.setCaption(post.getCaption());
        payload.setCreatedAt(post.getCreatedAt());
        payload.setUpdatedAt(post.getUpdatedAt());
        payload.setLastModifiedBy(post.getLastModifiedBy());
        payload.setImageUrl(post.getVideoUrl());
        return payload;
    }
}
