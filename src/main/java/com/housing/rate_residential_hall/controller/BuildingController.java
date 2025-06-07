package com.housing.rate_residential_hall.controller;

import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/building")
public class BuildingController {

  private BuildingService buildingService;

  public BuildingController(BuildingService buildingService){
    this.buildingService = buildingService;
  }

  @GetMapping("/search")
  public ResponseEntity<List<Building>> searchBuilding(@RequestParam String searchText){
    List<Building> matchedBuildings = buildingService.searchBuilding(searchText);
    if (!matchedBuildings.isEmpty()){
      return ResponseEntity.ok().body(matchedBuildings);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Building>> getAllBuildings() {
    List<Building> allBuildings = buildingService.getAllBuildings();
    return ResponseEntity.ok().body(allBuildings);
  }
}
