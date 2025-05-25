package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
