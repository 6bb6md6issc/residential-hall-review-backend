package com.housing.rate_residential_hall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "_buildings")
@Getter
@Setter
public class Building {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "building_name", nullable = false)
  private String buildingName;

  public Building(String buildingName){
    this.buildingName = buildingName;
  }
}