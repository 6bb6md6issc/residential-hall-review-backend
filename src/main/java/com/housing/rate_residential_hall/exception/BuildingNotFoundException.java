package com.housing.rate_residential_hall.exception;

public class BuildingNotFoundException extends RuntimeException {
  public BuildingNotFoundException(String message) {
    super(message);
  }
}
