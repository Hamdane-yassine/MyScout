package com.hamdane.feedsvc.client;

import com.hamdane.feedsvc.payload.JwtAuthenticationResponse;
import com.hamdane.feedsvc.payload.ServiceLoginRequest;
import com.hamdane.feedsvc.payload.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient("MyScout-AuthSvc")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "login")
    ResponseEntity<JwtAuthenticationResponse> login(@RequestBody ServiceLoginRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/users/summary/in")
    ResponseEntity<List<UserSummary>> findByUsernameIn(@RequestHeader("Authorization") String token, @RequestBody List<String> usernames);
}
