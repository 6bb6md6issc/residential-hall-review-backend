package com.housing.rate_residential_hall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.housing.rate_residential_hall.dto.CreateRatingDto;
import com.housing.rate_residential_hall.dto.RatingDto;
import com.housing.rate_residential_hall.service.RatingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
          @RequestPart("file") MultipartFile file,
          @RequestParam("rating_data") String json
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
  public ResponseEntity getAllRating(@PathVariable UUID buildingId){
    List<RatingDto> ratingDtoList = ratingService.getRating(buildingId);
    return ResponseEntity.ok().body(ratingDtoList);
  }

  



}
