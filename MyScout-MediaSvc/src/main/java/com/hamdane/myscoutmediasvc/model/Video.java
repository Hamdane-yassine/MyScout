package com.hamdane.myscoutmediasvc.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Video {

    @Id
    private String id;

    @CreatedBy
    private String username;

    @CreatedDate
    private Instant createdAt;

    private String title;
    private String description;

    private String objectName;

    @NonNull
    private AtomicInteger likes = new AtomicInteger(0);

    @NonNull
    private AtomicInteger disLikes = new AtomicInteger(0);
    private List<String> tags;

    @NonNull
    private String url;
    private AtomicInteger viewCount = new AtomicInteger(0);
    private List<Comment> comments = new ArrayList<>();

    public int likeCount() {
        return likes.get();
    }

    public int disLikeCount() {
        return disLikes.get();
    }

    public void increaseViewCount() {
        viewCount.incrementAndGet();
    }

    public void increaseLikeCount() {
        likes.incrementAndGet();
    }

    public void decreaseLikeCount() {
        likes.decrementAndGet();
    }

    public void increaseDisLikeCount() {
        disLikes.incrementAndGet();
    }

    public void decreaseDisLikeCount() {
        disLikes.decrementAndGet();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
