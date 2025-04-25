package com.identlite.api.repositories;

import com.identlite.api.models.Amenity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findByName(String name);
}
