package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.dto.*;
import com.housing.rate_residential_hall.dto.mapper.RatingMapper;
import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Image;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.BuildingNotFoundException;
import com.housing.rate_residential_hall.exception.RatingAlreadyExistsException;
import com.housing.rate_residential_hall.exception.RatingNotFoundException;
import com.housing.rate_residential_hall.repository.BuildingRepository;
import com.housing.rate_residential_hall.repository.ImageRepository;
import com.housing.rate_residential_hall.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

  private final RatingRepository ratingRepository;
  private final BuildingRepository buildingRepository;
  private final ImageRepository imageRepository;
  private final ImageService imageService;
  private final RatingMapper ratingMapper;
  private final AuthService authService;

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
      imageService.uploadNewImage(user, building, newRating, file);
    }
  }
  
  public GetRatingResponse getRating(UUID buildingId){
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(()-> new BuildingNotFoundException("No such building"));
    List<Rating> ratings = ratingRepository.findByBuilding(building);
    List<RatingDto> ratingDtos = ratings.stream()
            .map(rating -> ratingMapper.toDto(rating))
            .collect(Collectors.toList());
    return new GetRatingResponse(building.getBuildingName(), building.getId(), ratingDtos);
  }

  public void deleteRating(UUID ratingId){
    Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFoundException("No Such Rating"));

    authService.verifyAuthenticatedUser(rating.getUser().getId());

    Optional<Image> image = imageRepository.findByRating(rating);
    if(image.isPresent()){
      imageService.deleteFile(image.get());
    }
    ratingRepository.deleteById(ratingId);
  }

  @Transactional
  public void updateRating(UUID ratingId, UpdateRatingDto dto){
    Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(()-> new RatingNotFoundException("No Such Rating"));
    authService.verifyAuthenticatedUser(rating.getUser().getId());

    rating.setContent(dto.getContent());
    rating.setUpdatedAt(LocalDateTime.now());
    rating.setStartYear(dto.getStartYear());
    rating.setRatingValue(dto.getRatingValue());

    ratingRepository.save(rating);
  }

  public List<MyRatingDto> getAllRatingByUser(){
    User user = authService.getAuthenticatedUser();
    List<Rating> ratings = ratingRepository.findByUser(user);
    return ratings
            .stream()
            .map(ratingMapper::toMyRatingDto)
            .collect(Collectors.toList());
  }

  public MySpecificRatingResponse getBuildingSpecificRating(UUID buildingId) {
    UUID userId = authService.getAuthenticatedUser().getId();
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new BuildingNotFoundException("No Such Building"));
    Rating rating = ratingRepository.findByBuildingIdAndUserId(buildingId, userId)
            .orElse(null);
    RatingDto dto = (rating != null) ? ratingMapper.toDto(rating) : null;
    MySpecificRatingResponse response = new MySpecificRatingResponse(dto, building);
    return response;
  }
}
