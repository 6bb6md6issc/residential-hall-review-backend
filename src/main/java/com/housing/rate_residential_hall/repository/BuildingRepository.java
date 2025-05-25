package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Building;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends CrudRepository<Building, UUID> {
  Optional<Building> findById(UUID id);
}
