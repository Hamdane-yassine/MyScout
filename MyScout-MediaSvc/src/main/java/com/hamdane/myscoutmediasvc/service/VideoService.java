package com.hamdane.myscoutmediasvc.service;

import com.hamdane.myscoutmediasvc.model.Video;
import com.hamdane.myscoutmediasvc.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;

    private final S3Service s3Service;

    public Video uploadVideo(MultipartFile file) {
        var key = generateKey(file);
        String videoUrl = s3Service.uploadFile(file,key);
        var video = new Video();
        video.setUrl(videoUrl);
        video.setObjectName(key);

        log.info("uploaded video {}", videoUrl);
        return videoRepository.save(video);
    }

    private String generateKey(MultipartFile file){
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return String.format("%s.%s", UUID.randomUUID(), extension);
    }

}