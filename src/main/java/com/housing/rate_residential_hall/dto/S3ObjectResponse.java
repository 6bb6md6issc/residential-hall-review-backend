package com.housing.rate_residential_hall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class S3ObjectResponse {
  private byte[] bytes;
  private String contentType;
}
