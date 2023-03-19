package com.hamdane.feedsvc.service;


import com.hamdane.feedsvc.client.PostServiceClient;
import com.hamdane.feedsvc.exception.UnableToGetPostsException;
import com.hamdane.feedsvc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostServiceClient postServiceClient;

    public List<Post> findPostsIn(String token, List<String> ids) {
        ResponseEntity<List<Post>> response = postServiceClient.findPostsByIdIn("Bearer" + token, ids);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UnableToGetPostsException(String.format("unable to get posts for ids", ids));
        }
    }
}
