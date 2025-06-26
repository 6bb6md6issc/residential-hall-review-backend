package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetRatingResponse {

  @JsonProperty("building_name") private String buildingName;
  @JsonProperty("building_id") private UUID buildingId;
  private List<RatingDto> ratings;

  public GetRatingResponse(
          String buildingName,
          UUID buildingId,
          List<RatingDto> ratings
  ){
    this.buildingId = buildingId;
    this.buildingName = buildingName;
    this.ratings = ratings;
  }
}
