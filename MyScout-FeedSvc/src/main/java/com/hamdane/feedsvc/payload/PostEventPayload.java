package com.hamdane.feedsvc.payload;

import com.hamdane.feedsvc.messaging.PostEventType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PostEventPayload {

    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String lastModifiedBy;
    private String videoUrl;
    private String caption;
    private PostEventType eventType;

}
