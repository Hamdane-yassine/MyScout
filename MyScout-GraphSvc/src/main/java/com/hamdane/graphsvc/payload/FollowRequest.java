package com.hamdane.graphsvc.payload;


import lombok.Data;

@Data
public class FollowRequest {

    UserPayload follower;
    UserPayload following;
}
