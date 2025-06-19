package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.repository.BuildingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BuildingServiceTests {

  @Mock
  private BuildingRepository buildingRepository;

  @InjectMocks
  private BuildingService buildingService;

  @Test
  public void BuildingService_searchBuilding_returnMoreThanOneBuilding(){
    // Arrange
    String searchText = "hall";
    List<Building> buildings = List.of(
            new Building("Richards Hall"),
            new Building("Wasatch Hall")
    );
    when(buildingRepository.searchBuildingByText(searchText)).thenReturn(buildings);
    // Act
    List<Building> searchResult = buildingService.searchBuilding(searchText);
    // Assert
    Assertions.assertNotNull(searchResult);
    Assertions.assertEquals(2, searchResult.size());
  }

  @Test
  public void BuildingService_getAllBuildings_returnMoreThanOneBuilding(){
    // Arrange
    List<Building> buildings = List.of(
            new Building("Richards Hall"),
            new Building("Wasatch Hall")
    );
    when(buildingRepository.findAll()).thenReturn(buildings);
    // Act
    List<Building> searchResult = buildingService.getAllBuildings();
    // Assert
    Assertions.assertNotNull(searchResult);
    Assertions.assertEquals(2, searchResult.size());
  }

}
