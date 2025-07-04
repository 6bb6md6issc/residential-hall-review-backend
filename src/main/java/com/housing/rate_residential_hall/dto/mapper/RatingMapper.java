package com.housing.rate_residential_hall.dto.mapper;

import com.housing.rate_residential_hall.dto.MyRatingDto;
import com.housing.rate_residential_hall.dto.RatingDto;
import com.housing.rate_residential_hall.entity.Rating;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RatingMapper {

  public RatingDto toDto(Rating rating){
    return new RatingDto(
            rating.getId(),
            rating.getUser().getId(),
            rating.getBuilding().getId(),
            rating.getBuilding().getBuildingName(),
            rating.getContent(),
            rating.getCreatedAt(),
            rating.getStartYear(),
            rating.getRatingValue()
    );
  }

  public MyRatingDto toMyRatingDto(Rating rating){
    return new MyRatingDto(
            rating.getId(),
            rating.getBuilding().getId(),
            rating.getBuilding().getBuildingName(),
            rating.getCreatedAt()
    );
  }
}
