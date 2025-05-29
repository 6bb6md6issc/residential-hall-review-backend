package com.housing.rate_residential_hall.controller;

import com.housing.rate_residential_hall.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/image")
@RestController
public class ImageController {

  private ImageService imageService;

  public ImageController(ImageService imageService){
    this.imageService = imageService;
  }

  @GetMapping("/{ratingId}")
  public ResponseEntity<byte[]> getRatingImage(@PathVariable UUID ratingId){
    byte[] file = imageService.getRatingImage(ratingId);
    return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(file);

  }
//
//  @DeleteMapping("/{ratingId}")
//  public ResponseEntity deleteRatingImage(@PathVariable UUID ratingId) {
//
//  }
}
