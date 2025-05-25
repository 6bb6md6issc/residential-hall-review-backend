package com.housing.rate_residential_hall.repository;

import com.housing.rate_residential_hall.entity.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ImageRepository extends CrudRepository<Image, UUID> {

}
