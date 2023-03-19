package com.hamdane.myscoutmediasvc.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String key);

    void deleteFile(String fileName);

}
