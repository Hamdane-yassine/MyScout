package com.hamdane.postsvc.controller;


import com.hamdane.postsvc.dto.ApiResponse;
import com.hamdane.postsvc.dto.PostRequest;
import com.hamdane.postsvc.model.Post;
import com.hamdane.postsvc.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostApi {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/posts/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "Post created successfully"));
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user) {
        postService.deletePost(id, user.getName());
    }

    @GetMapping("/posts/me")
    public ResponseEntity<?> findCurrentUserPosts(@AuthenticationPrincipal Principal principal) {
        List<Post> posts = postService.postsByUsername(principal.getName());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{username}")
    public ResponseEntity<?> findUserPosts(@PathVariable("username") String username) {
        List<Post> posts = postService.postsByUsername(username);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts/in")
    public ResponseEntity<?> findPostsByIdIn(@RequestBody List<String> ids) {
        List<Post> posts = postService.postsByIdIn(ids);
        return ResponseEntity.ok(posts);
    }
}
