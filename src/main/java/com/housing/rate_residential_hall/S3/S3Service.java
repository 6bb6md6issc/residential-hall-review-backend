package com.housing.rate_residential_hall.S3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

  private final S3Client s3;

  @Value("${aws.bucketName}")
  private String bucketName;

  public S3Service(S3Client s3){
    this.s3 = s3;
  }

  public byte[] getObject(String bucketName, String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

    ResponseInputStream<GetObjectResponse> res = s3.getObject(getObjectRequest);
    try {
      return res.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void putObject(String key, byte[] file) {
    PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType("image/jpeg")
            .build();
    s3.putObject(objectRequest, RequestBody.fromBytes(file));
  }
}
