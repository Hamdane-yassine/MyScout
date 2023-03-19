package com.hamdane.myscoutauthsvc.controller;


import com.hamdane.myscoutauthsvc.dto.UserDto;
import com.hamdane.myscoutauthsvc.exception.ResourceNotFoundException;
import com.hamdane.myscoutauthsvc.model.User;
import com.hamdane.myscoutauthsvc.service.userManager.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping("/users/me/picture")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProfilePicture(@RequestBody String profilePicture, @AuthenticationPrincipal User user) {

        userService.updateProfilePicture(profilePicture, user.getId());
        return ResponseEntity.ok("Profile picture updated successfully");
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);
        return userService.findUser(username).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getCurrentUser(@AuthenticationPrincipal User user) {
        return convertTo(user);
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);
        return userService.findUser(username).map(user -> ResponseEntity.ok(convertTo(user))).orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @PostMapping(value = "/users/summary/in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummaries(@RequestBody List<String> usernames) {
        log.info("retrieving summaries for {} usernames", usernames.size());

        List<UserDto> summaries = userService.findByUserIn(usernames).stream().map(this::convertTo).collect(Collectors.toList());

        return ResponseEntity.ok(summaries);

    }

    private UserDto convertTo(User user) {
        return UserDto.builder().id(user.getId()).username(user.getUsername()).name(user.getUserProfile().getDisplayName()).profilePicture(user.getUserProfile().getProfilePictureUrl()).build();
    }
}
