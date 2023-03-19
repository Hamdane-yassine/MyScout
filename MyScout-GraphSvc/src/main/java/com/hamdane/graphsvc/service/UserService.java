package com.hamdane.graphsvc.service;

import com.hamdane.graphsvc.exception.UsernameAlreadyExistsException;
import com.hamdane.graphsvc.exception.UsernameNotExistsException;
import com.hamdane.graphsvc.model.NodeDegree;
import com.hamdane.graphsvc.model.User;
import com.hamdane.graphsvc.payload.PagedResult;
import com.hamdane.graphsvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            String message = String.format("username %s already exists", user.getUsername());
            throw new UsernameAlreadyExistsException(message);
        }
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    public void updateUser(User user) {
        userRepository.findByUsername(user.getUsername()).map(savedUser -> {
            savedUser.setName(user.getName());
            savedUser.setUsername(user.getUsername());
            savedUser.setProfilePic(user.getProfilePic());
            savedUser = userRepository.save(savedUser);
            return savedUser;
        }).orElseThrow(() -> new UsernameNotExistsException(String.format("user %s not exists", user.getUsername())));
    }

    @Transactional
    public void follow(User follower, User following) {
        User savedFollower = userRepository.findByUserId(follower.getUserId()).orElseGet(() -> {
            return this.addUser(follower);
        });
        User savedFollowing = userRepository.findByUserId(following.getUserId()).orElseGet(() -> {
            return this.addUser(following);
        });
        if (savedFollower.getFollowingUsers() == null) {
            savedFollower.setFollowingUsers(new HashSet<>());
        }
        savedFollower.getFollowingUsers().add(savedFollowing);
        userRepository.save(savedFollower);
    }

    public NodeDegree findNodeDegree(String username) {
        long out = userRepository.findOutDegree(username);
        long in = userRepository.findInDegree(username);
        return NodeDegree.builder().outDegree(out).inDegree(in).build();
    }

    public boolean isFollowing(String userA, String userb) {
        return userRepository.isFollowing(userA, userb);
    }

    public List<User> findFollowers(String username) {
        List<User> followers = userRepository.findFollowers(username);
        return followers;
    }

    public PagedResult<User> findPaginatedFollowers(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> followers = userRepository.findFollowers(username, pageable);
        return buildPagedResult(followers);
    }

    public List<User> findFollowing(String username) {
        List<User> following = userRepository.findFollowing(username);
        return following;
    }

    private PagedResult<User> buildPagedResult(Page<User> page) {
        return PagedResult.<User>builder().content(page.getContent()).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).page(page.getPageable().getPageNumber()).size(page.getSize()).last(page.isLast()).build();
    }
}
