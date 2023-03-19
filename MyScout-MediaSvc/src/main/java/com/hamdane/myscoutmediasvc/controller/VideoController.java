package com.hamdane.myscoutmediasvc.controller;

import com.hamdane.myscoutmediasvc.dto.VideoDto;
import com.hamdane.myscoutmediasvc.model.Video;
import com.hamdane.myscoutmediasvc.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/videos")
    @PreAuthorize("hasRole('USER')")
    public VideoDto uploadVideo(@RequestParam("video") MultipartFile file) {

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        log.info("received a request to upload file {} for user {}", filename);
        Video uploadedVideo = videoService.uploadVideo(file);

        return new VideoDto(uploadedVideo.getId(), uploadedVideo.getUsername(), uploadedVideo.getTitle(), uploadedVideo.getUrl());
    }

}

