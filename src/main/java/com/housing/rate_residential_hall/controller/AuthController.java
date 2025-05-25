package com.housing.rate_residential_hall.controller;

import com.housing.rate_residential_hall.dto.LoginUserDto;
import com.housing.rate_residential_hall.dto.RegisterUserDto;
import com.housing.rate_residential_hall.dto.VerifyEmailDto;
import com.housing.rate_residential_hall.repository.UserRepository;
import com.housing.rate_residential_hall.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

  private final AuthService authService;

  public AuthController(
          UserRepository userRepository,
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
    authService.authenticate(loginUserDto);
    return ResponseEntity.ok().body("successfully logged in");
  }

  @PostMapping("/verify")
  public ResponseEntity verify(@RequestBody VerifyEmailDto verifyEmailDto){
    authService.verifyEmail(verifyEmailDto);
    return ResponseEntity.ok().body("Account successfully verified");
  }


}
