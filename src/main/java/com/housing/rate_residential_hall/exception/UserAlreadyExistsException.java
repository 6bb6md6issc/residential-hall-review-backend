package com.housing.rate_residential_hall.exception;

public class UserAlreadyExistsException extends RuntimeException{
  private String message;

  public UserAlreadyExistsException(String message){
    super(message);
    this.message = message;
  }
}
