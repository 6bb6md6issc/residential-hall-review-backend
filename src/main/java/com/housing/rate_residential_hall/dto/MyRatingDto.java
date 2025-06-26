package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MyRatingDto {

  @JsonProperty("rating_id") private UUID ratingId;
  @JsonProperty("building_id") private UUID buildingId;
  @JsonProperty("building_name") private String buildingName;
  @JsonProperty("created_at") private LocalDateTime createdAt;

  public MyRatingDto(
          UUID ratingId,
          UUID buildingId,
          String buildingName,
          LocalDateTime createdAt
  ){
    this.ratingId = ratingId;
    this.buildingId = buildingId;
    this.buildingName = buildingName;
    this.createdAt = createdAt;
  }

}
