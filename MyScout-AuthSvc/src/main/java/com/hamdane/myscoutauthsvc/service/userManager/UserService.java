package com.hamdane.myscoutauthsvc.service.userManager;

import com.hamdane.myscoutauthsvc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getUsers();

    Optional<User> findUser(String username);

    public List<User> findByUserIn(List<String> usernames);

    public void updateProfilePicture(String uri, String id);
}
