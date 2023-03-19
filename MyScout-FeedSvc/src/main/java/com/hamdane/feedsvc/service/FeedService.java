package com.hamdane.feedsvc.service;

import com.datastax.oss.driver.api.core.cql.PagingState;
import com.hamdane.feedsvc.exception.ResourceNotFoundException;
import com.hamdane.feedsvc.model.Post;
import com.hamdane.feedsvc.model.UserFeed;
import com.hamdane.feedsvc.payload.SlicedResult;
import com.hamdane.feedsvc.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final PostService postService;
    private final AuthService authService;

    public SlicedResult<Post> getUserFeed(String username, Optional<String> pagingState) {
        CassandraPageRequest request = pagingState.map(pState -> CassandraPageRequest.of((Pageable) PageRequest.of(0, 20), (ByteBuffer) PagingState.fromString(pState))).orElse(CassandraPageRequest.first(20));
        Slice<UserFeed> page = feedRepository.findByUsername(username, request);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Feed not found for user %s", username));
        }
        String pageState = null;
        if (!page.isLast()) {
            pageState = Objects.requireNonNull(((CassandraPageRequest) page.getPageable()).getPagingState()).toString();
        }
        List<Post> posts = getPosts(page);
        return SlicedResult.<Post>builder().content(posts).isLast(page.isLast()).pagingState(pageState).build();
    }

    private List<Post> getPosts(Slice<UserFeed> page) {
        String token = authService.getAccessToken();
        List<String> postIds = page.stream().map(UserFeed::getPostId).collect(toList());
        List<Post> posts = postService.findPostsIn(token, postIds);
        List<String> usernames = posts.stream().map(Post::getUsername).distinct().toList();
        Map<String, String> usersProfilePics = authService.usersProfilePic(token, new ArrayList<>(usernames));
        posts.forEach(post -> post.setUserProfilePic(usersProfilePics.get(post.getUsername())));
        return posts;
    }
}
