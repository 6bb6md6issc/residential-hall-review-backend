package com.housing.rate_residential_hall.exception;

public class RatingNotFoundException extends RuntimeException {
  public RatingNotFoundException(String message) {
    super(message);
  }
}
