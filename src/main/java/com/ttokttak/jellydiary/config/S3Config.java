package com.ttokttak.jellydiary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.accessKey}")
    private String access;

    @Value("${aws.secretKey}")
    private String secret;

    @Value("${aws.region}")
    private String region;

    // access, secret key 이용해 aws 자격증명 제공
    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(access, secret);
        return StaticCredentialsProvider.create(awsCredentials);
    }

    // s3서비스를 이용하기 위한 S3Client 객체 생성
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider())
                .build();
    }
}