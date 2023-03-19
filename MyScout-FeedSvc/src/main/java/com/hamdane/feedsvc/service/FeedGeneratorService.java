package com.hamdane.feedsvc.service;

import com.hamdane.feedsvc.client.GraphServiceClient;
import com.hamdane.feedsvc.exception.UnableToGetFollowersException;
import com.hamdane.feedsvc.model.Post;
import com.hamdane.feedsvc.model.User;
import com.hamdane.feedsvc.model.UserFeed;
import com.hamdane.feedsvc.payload.PagedResult;
import com.hamdane.feedsvc.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedGeneratorService {

    private final AuthService tokenService;
    private final GraphServiceClient graphClient;
    private final FeedRepository feedRepository;

    public void addToFeed(Post post) {
        String token = tokenService.getAccessToken();
        boolean isLast = false;
        int page = 0;
        int size = 20;
        while (!isLast) {
            ResponseEntity<PagedResult<User>> response = graphClient.findFollowers("Bearer" + token, post.getUsername(), page, size);
            if (response.getStatusCode().is2xxSuccessful()) {
                PagedResult<User> result = response.getBody();
                assert result != null;
                result.getContent().stream().map(user -> convertTo(user, post)).forEach(feedRepository::insert);
                isLast = result.isLast();
                page++;
            } else {
                String message = String.format("unable to get followers for user %s", post.getUsername());
                throw new UnableToGetFollowersException(message);
            }
        }
    }

    private UserFeed convertTo(User user, Post post) {
        return UserFeed.builder().userId(user.getUserId()).username(user.getUsername()).postId(post.getId()).createdAt(post.getCreatedAt()).build();
    }
}
