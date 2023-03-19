package com.hamdane.feedsvc.controller;

import com.hamdane.feedsvc.model.Post;
import com.hamdane.feedsvc.payload.SlicedResult;
import com.hamdane.feedsvc.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @RequestMapping("/feed/{username}")
    public ResponseEntity<SlicedResult<Post>> getFeed(@PathVariable String username, @RequestParam(value = "ps", required = false) Optional<String> pagingState) {
        return ResponseEntity.ok(feedService.getUserFeed(username, pagingState));
    }
}
