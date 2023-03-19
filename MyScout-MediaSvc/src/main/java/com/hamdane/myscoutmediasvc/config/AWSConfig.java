package com.hamdane.myscoutmediasvc.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AWSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String access_key;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secret_key;


    @Value("${cloud.aws.region.static}")
    private String region;

    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        return credentials;
    }

    @Bean
    public AmazonS3Client amazonS3() {
        AmazonS3Client s3client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials())).withRegion(region).build();
        return s3client;
    }

}
