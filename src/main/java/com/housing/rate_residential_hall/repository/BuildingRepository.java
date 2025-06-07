package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
  Optional<Building> findById(UUID id);
  @Query("SELECT building FROM Building building where " +
          "LOWER(building.buildingName) LIKE LOWER(CONCAT('%', :searchText, '%'))"
  )
  List<Building> searchBuildingByText(@Param("searchText") String searchText);
  List<Building> findAll();
}
