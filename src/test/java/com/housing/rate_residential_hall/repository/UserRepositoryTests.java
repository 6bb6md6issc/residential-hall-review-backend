package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void UserRepository_findByEmail_returnMoreThanOneUser(){
    // Arrange
    User user1 = new User("test@gmail.com", "testPassword123");
    User user2 = new User("abc@gmail.com", "testPassword456");
    userRepository.save(user1);
    userRepository.save(user2);
    // Act
    Optional<User> findResult1 = userRepository.findByEmail("test@gmail.com");
    Optional<User> findResult3 = userRepository.findByEmail("notexist@gmail.com");
    // Assert
    Assertions.assertThat(findResult1).isPresent();
    Assertions.assertThat(findResult1.get().getPassword()).isEqualTo("testPassword123");
    Assertions.assertThat(findResult3).isNotPresent();
  }
}
