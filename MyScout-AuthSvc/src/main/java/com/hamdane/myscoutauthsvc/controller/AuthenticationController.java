package com.hamdane.myscoutauthsvc.controller;

import com.hamdane.myscoutauthsvc.dto.AuthenticationRequest;
import com.hamdane.myscoutauthsvc.dto.RegisterRequest;
import com.hamdane.myscoutauthsvc.exception.BadRequestException;
import com.hamdane.myscoutauthsvc.exception.EmailAlreadyExistsException;
import com.hamdane.myscoutauthsvc.exception.UsernameAlreadyExistsException;
import com.hamdane.myscoutauthsvc.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("creating user {}", request.getUsername());
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}
