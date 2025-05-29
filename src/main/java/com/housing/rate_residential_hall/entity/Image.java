package com.housing.rate_residential_hall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "_images")
@Getter
@Setter
public class Image {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "building_id", referencedColumnName = "id")
  private Building building;

  @Column(nullable = false)
  private LocalDateTime uploadedAt;

  @ManyToOne
  @JoinColumn(name = "rating_id", referencedColumnName = "id")
  private Rating rating;

  public Image(){};

  public Image(
          User user,
          Building building,
          LocalDateTime uploadedAt,
          Rating rating
  ){
    this.user = user;
    this.building = building;
    this.uploadedAt = uploadedAt;
    this.rating = rating;
  }

}
