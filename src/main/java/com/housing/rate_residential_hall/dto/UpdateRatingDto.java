package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UpdateRatingDto {
  private String content;
  @JsonProperty("start_year") private int startYear;
  @JsonProperty("rating_value") private int ratingValue;

  public UpdateRatingDto(
          String content,
          int startYear,
          int ratingValue
  ){
    this.content = content;
    this.startYear = startYear;
    this.ratingValue = ratingValue;
  }
}
