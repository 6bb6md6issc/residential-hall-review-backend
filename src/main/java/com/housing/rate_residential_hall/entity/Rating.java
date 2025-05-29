package com.housing.rate_residential_hall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "_ratings")
@Getter
@Setter
public class Rating {

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "building_id", referencedColumnName = "id")
  private Building building;

  @Column(name = "start_year", nullable = false)
  private int startYear;

  @Column(name = "rating_value", nullable = false)
  private int ratingValue;

  public Rating(){}

  public Rating(
          String content,
          User user,
          Building building,
          LocalDateTime createdAt,
          int startYear,
          int ratingValue
  ) {
    this.content = content;
    this.building = building;
    this.startYear = startYear;
    this.ratingValue = ratingValue;
    this.createdAt = createdAt;
    this.user = user;
  }
}

