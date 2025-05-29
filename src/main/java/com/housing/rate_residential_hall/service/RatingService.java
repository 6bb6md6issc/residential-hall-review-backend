package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.S3.S3Service;
import com.housing.rate_residential_hall.dto.CreateRatingDto;
import com.housing.rate_residential_hall.dto.RatingDto;
import com.housing.rate_residential_hall.dto.mapper.RatingMapper;
import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Image;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.BuildingNotFoundException;
import com.housing.rate_residential_hall.exception.RatingAlreadyExistsException;
import com.housing.rate_residential_hall.repository.BuildingRepository;
import com.housing.rate_residential_hall.repository.ImageRepository;
import com.housing.rate_residential_hall.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

  private final RatingRepository ratingRepository;
  private final BuildingRepository buildingRepository;
  private final ImageRepository imageRepository;
  private final S3Service s3Service;
  private final ImageService imageService;
  private final RatingMapper ratingMapper;

  public void createNewRating(CreateRatingDto dto, MultipartFile file){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    Building building = buildingRepository.findById(dto.getBuildingId())
            .orElseThrow(()-> new BuildingNotFoundException("No such building"));
    //    already have that rating under that building?
    if (ratingRepository.existsByUserAndBuilding(user, building)) {
      throw new RatingAlreadyExistsException("rating already exists");
    };
    Rating newRating = new Rating(
            dto.getContent(),
            user,
            building,
            LocalDateTime.now(),
            dto.getStartYear(),
            dto.getRatingValue()
    );
    ratingRepository.save(newRating);

    if(file != null) {
      Image newImage = new Image(user, building, LocalDateTime.now(), newRating);
      imageRepository.save(newImage);

      imageService.validateFile(file);
      try {
        s3Service.putObject(
                "ratings/%s".formatted(newImage.getId()),
                file.getBytes()
        );
      } catch (IOException ex) {
        throw new RuntimeException("Failed to upload file");
      }
    }
  }
  
  public List<RatingDto> getRating(UUID buildingId){
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(()-> new BuildingNotFoundException("No such building"));
    List<Rating> ratings = ratingRepository.findByBuilding(building);
    return ratings.stream()
            .map(ratingMapper::toDto)
            .collect(Collectors.toList());
  }




}
