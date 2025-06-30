package com.housing.rate_residential_hall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.housing.rate_residential_hall.dto.*;
import com.housing.rate_residential_hall.service.RatingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/rating")
@RestController
public class RatingController {

  private RatingService ratingService;

  public RatingController(
          RatingService ratingService
  ){
    this.ratingService = ratingService;
  }

  @PostMapping(path = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity createNewRating(
          @RequestPart(value = "file", required = false) MultipartFile file,
          @RequestPart("rating_data") String json
  ){
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      CreateRatingDto dto = objectMapper.readValue(json, CreateRatingDto.class);
      ratingService.createNewRating(dto, file);
      return ResponseEntity.ok().body("new rating successfully created");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping(path = "/{ratingId}")
  public ResponseEntity deleteRating(@PathVariable UUID ratingId){
    ratingService.deleteRating(ratingId);
    return ResponseEntity.ok().body("successfully deleted rating");
  }

  @GetMapping(path = "/{buildingId}")
  public ResponseEntity getAllRating(
          @PathVariable UUID buildingId,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "6") int size,
          @RequestParam(defaultValue = "startYear") String sortBy,
          @RequestParam(defaultValue = "false") boolean ascending
  ){
    Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    GetRatingResponse getRatingResponse = ratingService.getRating(buildingId, pageable);
    return ResponseEntity.ok().body(getRatingResponse);
  }

  @PutMapping(path = "/{ratingId}")
  public ResponseEntity updateRatingData (
          @PathVariable UUID ratingId,
          @RequestBody String json
  ){
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      UpdateRatingDto dto = objectMapper.readValue(json, UpdateRatingDto.class);
      ratingService.updateRating(ratingId, dto);
      return ResponseEntity.ok().body("rating successfully updated");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping(path = "/my-ratings")
  public ResponseEntity getCurrentUserRating() {
    List<MyRatingDto> currentUserRatings = ratingService.getAllRatingByUser();
    return ResponseEntity.ok().body(currentUserRatings);
  }

  @GetMapping(path = "/my-ratings/{buildingId}")
  public ResponseEntity getBuildingSpecificRating(
          @PathVariable UUID buildingId
  ){
    MySpecificRatingResponse rating = ratingService.getBuildingSpecificRating(buildingId);
    return ResponseEntity.ok().body(rating);
  }
}
