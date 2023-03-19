package com.hamdane.myscoutauthsvc.service.authentication;

import com.hamdane.myscoutauthsvc.dto.AuthenticationRequest;
import com.hamdane.myscoutauthsvc.dto.AuthenticationResponse;
import com.hamdane.myscoutauthsvc.dto.RegisterRequest;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

}
