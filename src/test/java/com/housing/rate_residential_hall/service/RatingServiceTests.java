package com.housing.rate_residential_hall.service;

import com.housing.rate_residential_hall.dto.CreateRatingDto;
import com.housing.rate_residential_hall.entity.Building;
import com.housing.rate_residential_hall.entity.Rating;
import com.housing.rate_residential_hall.entity.User;
import com.housing.rate_residential_hall.repository.BuildingRepository;
import com.housing.rate_residential_hall.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTests {

  @Mock
  private BuildingRepository buildingRepository;

  @Mock
  private RatingRepository ratingRepository;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private RatingService ratingService;

  @Test
  void createNewRating_validInput_savesRatingAndUploadsImage() {
    User mockUser = new User();
    mockUser.setEmail("user@example.com");

    Authentication auth = mock(Authentication.class);
    when(auth.getPrincipal()).thenReturn(mockUser);

    SecurityContext context = mock(SecurityContext.class);
    when(context.getAuthentication()).thenReturn(auth);

    try (MockedStatic<SecurityContextHolder> mockedContextHolder = mockStatic(SecurityContextHolder.class)) {
      mockedContextHolder.when(SecurityContextHolder::getContext).thenReturn(context);

      // Mock DTO and dependencies
      CreateRatingDto dto = new CreateRatingDto();
      Building mockBuilding = new Building("Wasatch Hall");
      dto.setContent("Great building");
      dto.setBuildingId(mockBuilding.getId());
      dto.setStartYear(2022);
      dto.setRatingValue(5);

      MultipartFile mockFile = mock(MultipartFile.class);


      when(buildingRepository.findById(dto.getBuildingId())).thenReturn(Optional.of(mockBuilding));
      when(ratingRepository.existsByUserAndBuilding(mockUser, mockBuilding)).thenReturn(false);

      // Act
      ratingService.createNewRating(dto, mockFile);

      // Assert
      verify(ratingRepository, times(1)).save(any(Rating.class));
    }
  }
}
