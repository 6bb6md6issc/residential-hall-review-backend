package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends CrudRepository<Rating, UUID> {
  boolean existsByUserAndBuilding(User user, Building building);
  boolean existsById(UUID id);
  List<Rating> findByBuilding(Building building);
  void deleteById(UUID id);
  List<Rating> findByUser(User user);
}
