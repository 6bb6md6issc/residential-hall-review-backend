package com.housing.rate_residential_hall.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Service
public class ImageService {

  @Value("${maxsize}") private long MAX_FILE_SIZE;

  public void validateFile(MultipartFile file) {
    if(file != null){
      validateFileSize(file);
      validateFileExtension(file);
    }
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
