package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Image;
import com.housing.rate_residential_hall.entity.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends CrudRepository<Image, UUID> {
  Optional<Image> findByRating(Rating rating);
  void deleteById(UUID id);
}
