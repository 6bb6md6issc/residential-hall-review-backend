package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.dto.*;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.exception.CodeExpiredException;
import com.housing.rate_residential_hall.exception.UserAlreadyExistsException;
import com.housing.rate_residential_hall.exception.UserNotFoundException;
import com.housing.rate_residential_hall.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private EmailService emailService;

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthService authService;

  @Test
  public void AuthService_SignUp_shouldThrowError(){
    RegisterUserDto dto = new RegisterUserDto("test@gmail.com", "testPassword");
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));
    Assertions.assertThrows(UserAlreadyExistsException.class, () -> authService.signup(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_SignUp_shouldNotThrowError() throws MessagingException {
    // Arrange
    RegisterUserDto dto = new RegisterUserDto("test@gmail.com", "testPassword");
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
    doNothing().when(emailService)
            .sendEmail(any(String.class), any(String.class), any(String.class));
    //Act
    authService.signup(dto);

    //Assert
    ArgumentCaptor<User> captorUser = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captorUser.capture());
    User savedUser = captorUser.getValue();
    Assertions.assertNotNull(savedUser);
    Assertions.assertEquals(savedUser.getEmail(), "test@gmail.com");
  }

  @Test
  public void AuthService_requestResetPassword_throwUserNotFoundError() {
    // Arrange
    RequestResetPasswordDto dto = new RequestResetPasswordDto("test@gmail.com");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenThrow(new UserNotFoundException("User Not Found"));
    //Act & Assert
    Assertions.assertThrows(UserNotFoundException.class, () -> authService.requestResetPassword(dto));
    verify(userRepository, never()).save(any());
  }

    @Test
  public void AuthService_requestResetPassword_shouldNotThrowError() throws MessagingException {
    // Arrange
    RequestResetPasswordDto dto = new RequestResetPasswordDto("test@gmail.com");
    User user = new User("test@gmail.com", "testPassword");
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    doNothing().when(emailService)
            .sendEmail(any(String.class), any(String.class), any(String.class));
    //Act
    authService.requestResetPassword(dto);

    //Assert
    Assertions.assertNotNull(user.getResetPasswordCode());
    Assertions.assertNotNull(user.getResetPasswordExpiration());
    verify(userRepository).save(user);
  }

  @Test
  public void AuthService_verifyEmail_throwUserNotFoundError() {
    // Arrange
    VerifyEmailDto dto = new VerifyEmailDto("test@gmail.com", "123456");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenThrow(new UserNotFoundException("User Not Found"));
    //Act & Assert
    Assertions.assertThrows(UserNotFoundException.class, () -> authService.verifyEmail(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_verifyEmail_throwCodeExpiredError() {
    // Arrange
    VerifyEmailDto dto = new VerifyEmailDto("test@gmail.com", "123456");
    User user = new User("test@gmail.com", "testPassword");
    user.setRegisterVerificationExpiration(LocalDateTime.of(2020, 1, 1, 1, 1));
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    //Act Assert
    Assertions.assertThrows(CodeExpiredException.class, () -> authService.verifyEmail(dto));
    verify(userRepository, never()).save(user);
  }

  @Test
  public void AuthService_verifyEmail_noErrorThrown() {
    // Arrange
    VerifyEmailDto dto = new VerifyEmailDto("test@gmail.com", "123456");
    User user = new User("test@gmail.com", "testPassword");
    user.setRegisterVerificationCode("123456");
    user.setRegisterVerificationExpiration(LocalDateTime.of(6666, 1, 1, 1, 1));
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    //Act
    authService.verifyEmail(dto);
    // Assert
    ArgumentCaptor<User> userSaveCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userSaveCaptor.capture());
    User capturedUser = userSaveCaptor.getValue();
    Assertions.assertNull(capturedUser.getRegisterVerificationExpiration());
    Assertions.assertTrue(capturedUser.isEnabled());
  }

  @Test
  public void AuthService_verifyEmail_throwBadCredentialError() {
    // Arrange
    VerifyEmailDto dto = new VerifyEmailDto("test@gmail.com", "123456");
    User user = new User("test@gmail.com", "testPassword");
    user.setRegisterVerificationCode("abcdef");
    user.setRegisterVerificationExpiration(LocalDateTime.of(6666, 1, 1, 1, 1));
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    // Assert
    Assertions.assertThrows(BadCredentialsException.class, () -> authService.verifyEmail(dto));
  }

  @Test
  public void AuthService_resetPassword_throwUserNotFoundError() {
    // Arrange
    ResetPasswordDto dto = new ResetPasswordDto("test@gmail.com", "123456", "newPassword");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenThrow(new UserNotFoundException("User Not Found"));
    //Act & Assert
    Assertions.assertThrows(UserNotFoundException.class, () -> authService.resetPassword(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_resetPassword_throwCodeExpiredError() {
    // Arrange
    ResetPasswordDto dto = new ResetPasswordDto("test@gmail.com", "123456", "newPassword");
    User user = new User("test@gmail.com", "testPassword");
    user.setResetPasswordExpiration(LocalDateTime.of(2020, 1, 1, 1, 1));
    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(user));
    //Act & Assert
    Assertions.assertThrows(CodeExpiredException.class, () -> authService.resetPassword(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_resetPassword_throwInvalidCredential() {
    // Arrange
    ResetPasswordDto dto = new ResetPasswordDto("test@gmail.com", "123456", "newPassword");
    User user = new User("test@gmail.com", "testPassword");
    user.setResetPasswordExpiration(LocalDateTime.of(2099, 1, 1, 1, 1));
    user.setResetPasswordCode("abcdef");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(user));
    //Act & Assert
    Assertions.assertThrows(BadCredentialsException.class, () -> authService.resetPassword(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_resetPassword_Successfully_reset_password() {
    // Arrange
    ResetPasswordDto dto = new ResetPasswordDto("test@gmail.com", "123456", "newPassword");
    User user = new User("test@gmail.com", "testPassword");
    user.setResetPasswordExpiration(LocalDateTime.of(2099, 1, 1, 1, 1));
    user.setResetPasswordCode("123456");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(user));
    when(passwordEncoder.encode(dto.getNewPassword())).thenReturn(dto.getNewPassword());
    // Act
    authService.resetPassword(dto);
    // Assert
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());
    User capturedUser = userCaptor.getValue();
    Assertions.assertEquals(dto.getNewPassword(), capturedUser.getPassword());
  }

  @Test
  public void AuthService_authenticate_throwUserNotFoundError() {
    // Arrange
    LoginUserDto dto = new LoginUserDto("test@gmail.com", "testPassword");
    when(userRepository.findByEmail(dto.getEmail()))
            .thenThrow(new UserNotFoundException("User Not Found"));
    //Act & Assert
    Assertions.assertThrows(UserNotFoundException.class, () -> authService.authenticate(dto));
    verify(userRepository, never()).save(any());
  }

  @Test
  public void AuthService_authenticate_SuccessfullyLoggedIn(){
    // Arrange
    LoginUserDto dto = new LoginUserDto("test@gmail.com", "testPassword");
    User user = new User("test@gmail.com", "testPassword");
    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
    .thenReturn(mock(Authentication.class));

    when(jwtService.generateToken(user)).thenReturn("jwt");
    // Act
    LoginResponse loginResponse = authService.authenticate(dto);

    // Assert
    Assertions.assertNotNull(loginResponse);
    Assertions.assertEquals("jwt", loginResponse.getToken());
    Assertions.assertEquals(user.getEmail(), loginResponse.getEmail());
  }
}
