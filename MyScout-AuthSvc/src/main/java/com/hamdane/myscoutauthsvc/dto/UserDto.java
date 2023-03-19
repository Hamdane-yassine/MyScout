package com.hamdane.myscoutauthsvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String id;
    private String username;
    private String name;
    private String profilePicture;

}
