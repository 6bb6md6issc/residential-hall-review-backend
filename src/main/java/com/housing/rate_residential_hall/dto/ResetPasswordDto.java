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
public class ResetPasswordDto {
  private String email;
  private String token;
  @JsonProperty("new_password")
  private String newPassword;
}
