package com.housing.rate_residential_hall.controller;

import com.housing.rate_residential_hall.dto.UploadNewImageDto;
import com.housing.rate_residential_hall.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/image")
@RestController
public class ImageController {

  private ImageService imageService;

  public ImageController(ImageService imageService){
    this.imageService = imageService;
  }

  @GetMapping("/{ratingId}")
  public ResponseEntity<?> getRatingImage(@PathVariable UUID ratingId){
    Optional<byte[]> file = imageService.getRatingImage(ratingId);
    if (file.isPresent()) {
      return ResponseEntity.ok()
              .contentType(MediaType.IMAGE_JPEG)
              .body(file.get());
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping(path = "/{ratingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity uploadOrUpdateRatingImage(
          @PathVariable UUID ratingId,
          @RequestPart MultipartFile file
  ){
    imageService.updateFileByRatingId(ratingId, file);
    return ResponseEntity.ok().body("Image Successfully Uploaded/Updated");
  }

  @DeleteMapping("/{ratingId}")
  public ResponseEntity deleteRatingImage(@PathVariable UUID ratingId) {
    imageService.deleteFile(ratingId);
    return ResponseEntity.ok().body("Image Successfully Deleted");
  }

}
