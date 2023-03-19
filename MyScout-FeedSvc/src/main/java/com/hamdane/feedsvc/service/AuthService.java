package com.hamdane.feedsvc.service;

import com.hamdane.feedsvc.client.AuthServiceClient;
import com.hamdane.feedsvc.exception.UnableToGetAccessTokenException;
import com.hamdane.feedsvc.exception.UnableToGetUsersException;
import com.hamdane.feedsvc.payload.JwtAuthenticationResponse;
import com.hamdane.feedsvc.payload.ServiceLoginRequest;
import com.hamdane.feedsvc.payload.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthServiceClient authClient;
    private final ServiceLoginRequest serviceLoginRequest;


    public String getAccessToken() {
        ResponseEntity<JwtAuthenticationResponse> response = authClient.login(serviceLoginRequest);
        if (!response.getStatusCode().is2xxSuccessful()) {
            String message = String.format("unable to get access token for service account, %s", response.getStatusCode());
            throw new UnableToGetAccessTokenException(message);
        }
        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    public Map<String, String> usersProfilePic(String token, List<String> usernames) {
        ResponseEntity<List<UserSummary>> response = authClient.findByUsernameIn("Bearer" + token, usernames);
        if (!response.getStatusCode().is2xxSuccessful()) {
            String message = String.format("unable to get user summaries %s", response.getStatusCode());
            throw new UnableToGetUsersException(message);
        }
        return Objects.requireNonNull(response.getBody()).stream().collect(toMap(UserSummary::getUsername, UserSummary::getProfilePicture));
    }
}
