package com.hamdane.graphsvc.controller;


import com.hamdane.graphsvc.model.User;
import com.hamdane.graphsvc.payload.ApiResponse;
import com.hamdane.graphsvc.payload.FollowRequest;
import com.hamdane.graphsvc.service.UserService;
import com.hamdane.graphsvc.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/followers")
    public ResponseEntity<?> follow(@RequestBody FollowRequest request) {
        userService.follow(User.builder().userId(request.getFollower().getId()).username(request.getFollower().getUsername()).name(request.getFollower().getName()).profilePic(request.getFollower().getProfilePicture()).build(),

                User.builder().userId(request.getFollowing().getId()).username(request.getFollowing().getUsername()).name(request.getFollowing().getName()).profilePic(request.getFollowing().getProfilePicture()).build());

        String message = String.format("user %s is following user %s", request.getFollower().getUsername(), request.getFollowing().getUsername());


        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @GetMapping("/users/{username}/degree")
    public ResponseEntity<?> findNodeDegree(@PathVariable String username) {

        return ResponseEntity.ok(userService.findNodeDegree(username));
    }

    @GetMapping("/users/{usernameA}/following/{usernameB}")
    public ResponseEntity<?> isFollwoing(@PathVariable String usernameA, @PathVariable String usernameB) {

        return ResponseEntity.ok(userService.isFollowing(usernameA, usernameB));
    }

    @GetMapping("/users/{username}/followers")
    public ResponseEntity<?> findFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.findFollowers(username));
    }

    @GetMapping("/users/paginated/{username}/followers")
    public ResponseEntity<?> findFollowersPaginated(@PathVariable String username, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return ResponseEntity.ok(userService.findPaginatedFollowers(username, page, size));
    }

    @GetMapping("/users/{username}/following")
    public ResponseEntity<?> findFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.findFollowing(username));
    }

}
