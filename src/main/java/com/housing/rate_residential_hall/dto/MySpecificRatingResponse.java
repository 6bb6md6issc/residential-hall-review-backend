package com.housing.rate_residential_hall.dto;

import com.housing.rate_residential_hall.entity.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MySpecificRatingResponse {
  private RatingDto ratingDto;
  private Building building;
}
