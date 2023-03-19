package com.hamdane.postsvc.service;

import com.hamdane.postsvc.dto.PostRequest;
import com.hamdane.postsvc.exception.NotAllowedException;
import com.hamdane.postsvc.messaging.PostEventSender;
import com.hamdane.postsvc.model.Post;
import com.hamdane.postsvc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostEventSender postEventSender;

    public Post createPost(PostRequest postRequest) {
        Post post = new Post(postRequest.getVideoUrl(), postRequest.getCaption());
        post = postRepository.save(post);
        postEventSender.sendPostCreated(post);
        return post;
    }

    public void deletePost(String postId, String username) {
        postRepository.findById(postId).map(post -> {
            if (!post.getUsername().equals(username)) {
                throw new NotAllowedException(username, "post id " + postId, "delete");
            }
            postRepository.delete(post);
            postEventSender.sendPostDeleted(post);
            return post;
        }).orElseThrow(() -> {
            return new ResourceNotFoundException(postId);
        });
    }

    public List<Post> postsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public List<Post> postsByIdIn(List<String> ids) {
        return postRepository.findByIdInOrderByCreatedAtDesc(ids);
    }
}
