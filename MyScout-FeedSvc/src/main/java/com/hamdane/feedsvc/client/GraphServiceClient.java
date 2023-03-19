package com.hamdane.feedsvc.client;


import com.hamdane.feedsvc.model.User;
import com.hamdane.feedsvc.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient("MyScout-GraphSvc")
public interface GraphServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/paginated/{username}/followers")
    ResponseEntity<PagedResult<User>> findFollowers(@RequestHeader("Authorization") String token, @PathVariable String username, @RequestParam int page, @RequestParam int size);
}
