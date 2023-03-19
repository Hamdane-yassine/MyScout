package com.hamdane.myscoutauthsvc.service.userManager;

import com.hamdane.myscoutauthsvc.exception.ResourceNotFoundException;
import com.hamdane.myscoutauthsvc.messaging.UserEventSender;
import com.hamdane.myscoutauthsvc.model.User;
import com.hamdane.myscoutauthsvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventSender userEventSender;


    @Override
    public List<User> getUsers() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUser(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByUserIn(List<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

    @Override
    public void updateProfilePicture(String url, String id) {
        log.info("update profile picture {} for user {}", url, id);
        userRepository.findById(id).map(user -> {
            String oldProfilePic = user.getUserProfile().getProfilePictureUrl();
            user.getUserProfile().setProfilePictureUrl(url);
            User savedUser = userRepository.save(user);
            userEventSender.sendUserUpdated(savedUser, oldProfilePic);
            return savedUser;
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("user id %s not found", id)));
    }
}
