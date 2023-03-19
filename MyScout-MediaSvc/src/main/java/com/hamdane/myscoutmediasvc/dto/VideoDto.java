package com.hamdane.myscoutmediasvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoDto {
    private String videoId;
    private String userId;
    private String videoName;
    private String url;
}
