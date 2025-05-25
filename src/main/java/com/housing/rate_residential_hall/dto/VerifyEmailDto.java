package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailDto {
  private String email;
  @JsonProperty("verification_code")
  private String verificationCode;
}
