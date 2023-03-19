package com.hamdane.myscoutmediasvc.repository;

import com.hamdane.myscoutmediasvc.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {

}
