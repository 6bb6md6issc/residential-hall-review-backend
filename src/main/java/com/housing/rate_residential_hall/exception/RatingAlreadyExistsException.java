package com.housing.rate_residential_hall.exception;

public class RatingAlreadyExistsException extends RuntimeException {
  public RatingAlreadyExistsException(String message) {
    super(message);
  }
}
