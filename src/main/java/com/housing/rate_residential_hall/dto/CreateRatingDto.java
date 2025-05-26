package com.housing.rate_residential_hall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRatingDto {
  private UUID buildingId;
  private String content;
  private int startYear;
  private int ratingValue;
}
