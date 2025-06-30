package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {

  private final BuildingRepository buildingRepository;

  public List<Building> searchBuilding(String searchText) {
    List<Building> matchedBuildings = buildingRepository.searchBuildingByText(searchText);
    return matchedBuildings;
  }

  public List<Building> getAllBuildings() {
    List<Building> allBuildings = buildingRepository.findAll();
    return allBuildings;
  }

  public Page<Building> getAllBuildingsPagination(Pageable pageable) {
    return buildingRepository.findAll(pageable);
  }
}
