package com.housing.rate_residential_hall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetRatingResponse {

  @JsonProperty("building_name") private String buildingName;
  @JsonProperty("building_id") private UUID buildingId;
  private List<RatingDto> ratings;
  @JsonProperty("total_pages")private int totalPages;
  @JsonProperty("current_page")private int currentPage;
  @JsonProperty("total_elements")private long totalElements;
  @JsonProperty("page_size")private int pageSize;

  public GetRatingResponse(
          String buildingName,
          UUID buildingId,
          Page<RatingDto> ratings
  ){
    this.buildingId = buildingId;
    this.buildingName = buildingName;
    this.ratings = ratings.getContent();
    this.totalPages = ratings.getTotalPages();
    this.currentPage = ratings.getNumber();
    this.totalElements = ratings.getTotalElements();
    this.pageSize = ratings.getSize();
  }
}
