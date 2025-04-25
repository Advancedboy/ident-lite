package com.identlite.api.repositories;

import com.identlite.api.models.Hotel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);
}

