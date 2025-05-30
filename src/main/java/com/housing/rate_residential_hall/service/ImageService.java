package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.S3.S3Service;
import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Image;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.ImageNotFoundException;
import com.housing.rate_residential_hall.exception.RatingNotFoundException;
import com.housing.rate_residential_hall.repository.ImageRepository;
import com.housing.rate_residential_hall.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDateTime;
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
  private final AuthService authService;

  public void validateFile(MultipartFile file) {
    if(file != null){
      validateFileSize(file);
      validateFileExtension(file);
    }
  }

  public Optional<byte[]> getRatingImage(UUID ratingId){
//    frontend logic <img src={} onError={...}>
//    return jpg/jpeg as byte[]
    Rating rating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFoundException("No Such Rating"));
    Optional<Image> image = imageRepository.findByRating(rating);

    if(image.isPresent()) {
      byte[] file = s3Service.getObject(
              "ratings/%s".formatted(image.get().getId())
      );
      return Optional.of(file);
    }
    return Optional.empty();
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

  public void deleteFile(UUID imageId){
    // only deleting the file but not other content
    Image image = imageRepository.findById(imageId)
            .orElseThrow(()-> new ImageNotFoundException("No Such Image"));
    authService.verifyAuthenticatedUser(image.getUser().getId());
    s3Service.deleteFile(
            "ratings/%s".formatted(imageId)
    );
    imageRepository.deleteById(imageId);
  }

  public void deleteFile(Image image){
    // overloaded method
    // image is guaranteed to be present
    // helper function for RatingService.deleteRating method
    authService.verifyAuthenticatedUser(image.getUser().getId());
    s3Service.deleteFile(
            "ratings/%s".formatted(image.getId())
    );
    imageRepository.deleteById(image.getId());
  }

  public void uploadNewImage(User user, Building building, Rating newRating, MultipartFile file) {
//    must be jpg, otherwise throw exception
    validateFile(file);
//    create and save Image to get a UUID id
    Image newImage = new Image(user, building, LocalDateTime.now(), newRating);
    imageRepository.save(newImage);

    try {
      s3Service.putObject(
              "ratings/%s".formatted(newImage.getId()),
              file.getBytes()
      );
    } catch (IOException ex) {
      throw new RuntimeException("Failed to upload file");
    }
  }

  public void updateFile(UUID imageId, MultipartFile file){
    Image image = imageRepository.findById(imageId)
            .orElseThrow(()-> new ImageNotFoundException("No Such Image"));

    authService.verifyAuthenticatedUser(image.getUser().getId());
    validateFile(file);
    try {
      s3Service.putObject(
              "rating/%s".formatted(imageId),
              file.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    image.setUploadedAt(LocalDateTime.now());
    imageRepository.save(image);
  }
}
