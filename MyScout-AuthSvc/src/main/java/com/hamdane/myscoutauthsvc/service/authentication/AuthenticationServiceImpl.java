package com.hamdane.myscoutauthsvc.service.authentication;

import com.hamdane.myscoutauthsvc.dto.AuthenticationRequest;
import com.hamdane.myscoutauthsvc.dto.AuthenticationResponse;
import com.hamdane.myscoutauthsvc.dto.RegisterRequest;
import com.hamdane.myscoutauthsvc.exception.EmailAlreadyExistsException;
import com.hamdane.myscoutauthsvc.exception.UsernameAlreadyExistsException;
import com.hamdane.myscoutauthsvc.messaging.UserEventSender;
import com.hamdane.myscoutauthsvc.model.*;
import com.hamdane.myscoutauthsvc.repository.TokenRepository;
import com.hamdane.myscoutauthsvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserEventSender userEventSender;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("registering user {}", request.getUsername());
        if (repository.existsByUsername(request.getUsername())) {
            log.warn("username {} already exists.", request.getUsername());
            throw new UsernameAlreadyExistsException(String.format("username %s already exists", request.getUsername()));
        }

        if (repository.existsByEmail(request.getEmail())) {
            log.warn("email {} already exists.", request.getEmail());
            throw new EmailAlreadyExistsException(String.format("email %s already exists", request.getEmail()));
        }

        var user = User.builder().active(true).username(request.getUsername()).userProfile(Profile.builder().displayName(request.getName()).build()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);

        userEventSender.sendUserCreated(savedUser);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = repository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var UserTokens = tokenRepository.findByUser(user);
        var validUserTokens = UserTokens.stream().filter(t -> !t.isExpired() && !t.isRevoked()).collect(Collectors.toList());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
