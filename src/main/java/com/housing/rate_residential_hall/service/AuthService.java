package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.dto.LoginResponse;
import com.housing.rate_residential_hall.dto.LoginUserDto;
import com.housing.rate_residential_hall.dto.RegisterUserDto;
import com.housing.rate_residential_hall.dto.VerifyEmailDto;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.CodeExpiredException;
import com.housing.rate_residential_hall.exception.UserAlreadyExistsException;
import com.housing.rate_residential_hall.exception.UserNotFoundException;
import com.housing.rate_residential_hall.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final AuthenticationManager authManager;
  private final JwtService jwtService;

  @Transactional
  public void signup(RegisterUserDto dto){
    if (userRepository.findByEmail(dto.getEmail()).isPresent()){
      throw new UserAlreadyExistsException("User Already Exists");
    }
    User user = new User(dto.getEmail(), dto.getPassword());
    user.setRegisterVerificationCode(generateCode());
    user.setRegisterVerificationExpiration(LocalDateTime.now().plusMinutes(15));
    user.setEnabled(false);
    sendVerificationCode(user);
    userRepository.save(user);
  }

  public LoginResponse authenticate(LoginUserDto dto) {
    User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    dto.getEmail(),
                    dto.getPassword()
            )
    );
    LoginResponse loginResponse = new LoginResponse(user.getEmail(), jwtService.generateToken(user));
    return loginResponse;
  }

  @Transactional
  public void verifyEmail(VerifyEmailDto verifyEmailDto){
    User user = userRepository.findByEmail(verifyEmailDto.getEmail())
            .orElseThrow(()-> new UserNotFoundException("User Not Found"));
    if (user.getRegisterVerificationExpiration().isBefore(LocalDateTime.now())) {
      throw new CodeExpiredException("Verification Code Expired");
    }
    if (user.getRegisterVerificationCode().equals(verifyEmailDto.getVerificationCode())){
      user.setEnabled(true);
      user.setRegisterVerificationExpiration(null);
      userRepository.save(user);
    } else {
      throw new BadCredentialsException("Invalid Verification Code");
    }
  }

  private void sendVerificationCode (User user) {
    String verificationCode = user.getRegisterVerificationCode();
    String htmlMessage = "<html>"
            + "<body style=\"font-family: Arial, sans-serif;\">"
            + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
            + "<h2 style=\"color: #333;\">Welcome!</h2>"
            + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
            + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
            + "<h3 style=\"color: #333;\">Verification Code:</h3>"
            + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
            + "</div>"
            + "</div>"
            + "</body>"
            + "</html>";
    String subject = "Verify Your Account";
    try {
      emailService.sendEmail(user.getEmail(), subject, htmlMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private String generateCode() {
    Random random = new Random();
    int code = random.nextInt(90000) + 10000;
    return String.valueOf(code);
  }

}
