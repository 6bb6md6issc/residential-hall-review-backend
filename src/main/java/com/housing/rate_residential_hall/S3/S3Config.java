package com.housing.rate_residential_hall.S3;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${aws.region}")
  private String awsRegion;

  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.secretAccessKey}")
  private String accessSecret;


  @Bean
  public S3Client s3Client() {
    AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, accessSecret);
    return S3Client
            .builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
  }
}

