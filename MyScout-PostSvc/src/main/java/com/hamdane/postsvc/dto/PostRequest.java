package com.hamdane.postsvc.dto;

import lombok.Data;

@Data
public class PostRequest {

    private String videoUrl;
    private String caption;
}
