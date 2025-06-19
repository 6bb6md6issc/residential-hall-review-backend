package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Building;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BuildingRepositoryTests {

  @Autowired
  private BuildingRepository buildingRepository;

  @Test
  public void BuildingRepository_findById_returnBuilding(){
    //Arrange
    Building building = new Building("test building name");
    Building savedBuilding = buildingRepository.save(building);
    //Act
    Building targetBuilding = buildingRepository.findById(savedBuilding.getId()).get();
    //Asset
    Assertions.assertThat(targetBuilding).isNotNull();
    Assertions.assertThat(targetBuilding.getBuildingName()).isEqualTo("test building name");
  }

  @Test
  public void BuildingRepository_findAll_returnBuilding(){
    //Arrange
    Building building1 = new Building("wasatch Hall");
    Building savedBuilding1 = buildingRepository.save(building1);
    Building building2 = new Building("Richards Hall");
    Building savedBuilding2 = buildingRepository.save(building2);
    //Act
    List<Building> buildings = buildingRepository.findAll();
    //Assert
    Assertions.assertThat(buildings).isNotNull();
    Assertions.assertThat(buildings.size()).isEqualTo(2);
  }

  @Test
  public void BuildingRepository_searchBuildingByText_returnBuildingList(){
    //Arrange
    Building building1 = new Building("wasatch Hall");
    Building savedBuilding1 = buildingRepository.save(building1);
    Building building2 = new Building("Richards Hall");
    Building savedBuilding2 = buildingRepository.save(building2);
    //Act
    List<Building> buildings1 = buildingRepository.searchBuildingByText("hall");
    List<Building> buildings2 = buildingRepository.searchBuildingByText("abcd");
    //Assert
    Assertions.assertThat(buildings1).isNotNull();
    Assertions.assertThat(buildings1.size()).isEqualTo(2);
    Assertions.assertThat(buildings2).isNotNull();
    Assertions.assertThat(buildings2.size()).isEqualTo(0);
  }
}
