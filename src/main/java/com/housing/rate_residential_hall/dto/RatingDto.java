package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RatingDto {

  @JsonProperty("rating_id")private UUID ratingId;
  @JsonProperty("user_id")private UUID userId;
  @JsonProperty("building_id")private UUID buildingId;
  @JsonProperty("building_name")private String buildingName;
  private String content;
  @JsonProperty("created_at") private LocalDateTime createdAt;
  @JsonProperty("start_year")private int startYear;
  @JsonProperty("rating_value")private int ratingValue;

  public RatingDto(
          UUID ratingId,
          UUID userId,
          UUID buildingId,
          String buildingName,
          String content,
          LocalDateTime createdAt,
          int startYear,
          int ratingValue
  ){
    this.ratingId = ratingId;
    this.userId = userId;
    this.buildingId = buildingId;
    this.buildingName = buildingName;
    this.content = content;
    this.createdAt = createdAt;
    this.startYear = startYear;
    this.ratingValue = ratingValue;
  }
}
