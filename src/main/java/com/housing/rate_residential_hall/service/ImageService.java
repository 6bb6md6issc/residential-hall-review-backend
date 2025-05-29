package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.S3.S3Service;
import com.housing.rate_residential_hall.entity.Image;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.exception.ImageNotFoundException;
import com.housing.rate_residential_hall.exception.RatingNotFoundException;
import com.housing.rate_residential_hall.repository.ImageRepository;
import com.housing.rate_residential_hall.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

  @Value("${maxsize}") private long MAX_FILE_SIZE;
  private final ImageRepository imageRepository;
  private final RatingRepository ratingRepository;
  private final S3Service s3Service;

  public void validateFile(MultipartFile file) {
    if(file != null){
      validateFileSize(file);
      validateFileExtension(file);
    }
  }

  public byte[] getRatingImage(UUID ratingId){
    Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFoundException("No Such Rating"));
    Image image = imageRepository.findByRating(rating)
            .orElseThrow(() -> new ImageNotFoundException("No Such Image "));
    byte[] file = s3Service.getObject(
            "ratings/%s".formatted(image.getId())
    );
    return file;
  }

  private void validateFileSize(MultipartFile file) {
    // file size limited to 2MB
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new IllegalArgumentException("File size exceeds limit of 2 MB.");
    }
  }

  private void validateFileExtension(MultipartFile file) {
    String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase(Locale.ROOT);
    if (!extension.equals("jpg") && !extension.equals("jpeg")) {
      throw new IllegalArgumentException("Only jpg/jpeg files are accepted");
    }
  }
}
