package com.housing.rate_residential_hall.controller;

import com.housing.rate_residential_hall.dto.*;
import com.housing.rate_residential_hall.repository.UserRepository;
import com.housing.rate_residential_hall.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

  private final AuthService authService;

  public AuthController(
          AuthService authService
  ){
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity register(@RequestBody RegisterUserDto registerUserDto) {
    authService.signup(registerUserDto);
    return ResponseEntity.ok().body("Successfully registered");
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginUserDto loginUserDto) {
    LoginResponse loginResponse = authService.authenticate(loginUserDto);
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/verify")
  public ResponseEntity verify(@RequestBody VerifyEmailDto verifyEmailDto){
    authService.verifyEmail(verifyEmailDto);
    return ResponseEntity.ok().body("Account successfully verified");
  }

  @PostMapping("/reset-password-request")
  public ResponseEntity requestResetPassword(
          @RequestBody RequestResetPasswordDto requestResetPasswordDto
  ) {
    authService.requestResetPassword(requestResetPasswordDto);
    return ResponseEntity.ok("Check Email to Reset Password");
  }

  @PostMapping("/reset-password")
  public ResponseEntity resetPassword(
          @RequestBody ResetPasswordDto resetPasswordDto
  ) {
    authService.resetPassword(resetPasswordDto);
    return ResponseEntity.ok("Successfully reset password");
  }
}
