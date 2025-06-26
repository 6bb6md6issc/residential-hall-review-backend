package com.housing.rate_residential_hall.dto;

import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UploadNewImageDto {

  private UUID ratingId;

  public UploadNewImageDto(UUID ratingId){
    this.ratingId = ratingId;
  }
}
