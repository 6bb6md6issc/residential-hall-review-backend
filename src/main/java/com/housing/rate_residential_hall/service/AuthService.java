package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.dto.*;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.CodeExpiredException;
import com.housing.rate_residential_hall.exception.UnauthorizedException;
import com.housing.rate_residential_hall.exception.UserAlreadyExistsException;
import com.housing.rate_residential_hall.exception.UserNotFoundException;
import com.housing.rate_residential_hall.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Value("${spring.frontend.host}")
  private String frontEndUrl;

  @Transactional
  public void signup(RegisterUserDto dto){
    if (userRepository.findByEmail(dto.getEmail()).isPresent()){
      throw new UserAlreadyExistsException("User Already Exists");
    }
    User user = new User(
            dto.getEmail(),
            passwordEncoder.encode(dto.getPassword())
    );
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
  public void requestResetPassword(RequestResetPasswordDto resetPasswordDto) {
    User user = userRepository.findByEmail(resetPasswordDto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    String resetPasswordCode = generateCode();
    user.setResetPasswordCode(resetPasswordCode);
    user.setResetPasswordExpiration(LocalDateTime.now().plusMinutes(15));
    userRepository.save(user);
    sendResetPasswordLink(user);
  }

  @Transactional
  public void resetPassword(ResetPasswordDto resetPasswordDto) {
    User user = userRepository.findByEmail(resetPasswordDto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    if (user.getResetPasswordExpiration().isBefore(LocalDateTime.now())){
      throw new CodeExpiredException("Reset Password Code Expired");
    }
    if (!user.getResetPasswordCode().equals(resetPasswordDto.getToken())){
      throw new BadCredentialsException("Invalid Reset Password Credential");
    }
    user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
    userRepository.save(user);
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

  public void verifyAuthenticatedUser(UUID targetUserId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    if (!targetUserId.equals(user.getId())){
      throw new UnauthorizedException("Unauthorized.");
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
      throw new RuntimeException("Error sending verification code");
    }
  }

  private void sendResetPasswordLink (User user) {
    String resetPasswordCode = user.getResetPasswordCode();

    String url = frontEndUrl + "/reset-password/" + resetPasswordCode;
    String htmlMessage = "<html>"
            + "<body style=\"font-family: Arial, sans-serif;\">"
            + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
            + "<h2 style=\"color: #333;\">Please use the following link to reset password</h2>"
            + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
            + "<a style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + url + "</a>"
            + "</div>"
            + "</div>"
            + "</body>"
            + "</html>";
    String subject = "Reset Password";
    try {
      emailService.sendEmail(user.getEmail(), subject, htmlMessage);
    } catch (MessagingException e) {
      throw new RuntimeException("Error sending reset password email");
    }
  }

  private String generateCode() {
    Random random = new Random();
    int code = random.nextInt(90000) + 10000;
    return String.valueOf(code);
  }

}
