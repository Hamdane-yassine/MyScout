package com.hamdane.myscoutmediasvc.service;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

public interface S3Service extends FileService {

    // create a bucket
    public void createS3Bucket(String bucketName);

    // list buckets
    public List<Bucket> listBuckets();

    // delete a bucket
    public void deleteBucket(String bucketName);

    // list All objects names
    public List<S3ObjectSummary> listObjects(String bucketName);

    // download an object
    public void downloadObject(String bucketName, String objectName);

    // delete multiple objects
    public void deleteMultipleObjects(String bucketName, List<String> objects);

    // moving an object between two buckets:
    public void moveObject(String bucketSourceName, String objectName, String bucketTargetName);
}
