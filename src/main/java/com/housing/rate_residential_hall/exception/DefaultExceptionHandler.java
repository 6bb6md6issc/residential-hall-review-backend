package com.housing.rate_residential_hall.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(value = UserAlreadyExistsException.class)
  public ResponseEntity handleUserAlreadyExistsException(UserAlreadyExistsException ex){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(value = DisabledException.class)
  public ResponseEntity handleDisabledException(DisabledException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account not Verified");
  }

  @ExceptionHandler(value = BadCredentialsException.class)
  public ResponseEntity handleBadCredentialsException(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
  }

  @ExceptionHandler(value = CodeExpiredException.class)
  public ResponseEntity hanldeCodeExpiredException(CodeExpiredException ex) {
    return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
  }

  @ExceptionHandler(value = RuntimeException.class)
  public ResponseEntity handleRuntimeException(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }
}
