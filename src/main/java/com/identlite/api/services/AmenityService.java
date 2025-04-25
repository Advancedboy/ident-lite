package com.identlite.api.services;

import com.identlite.api.dto.AmenityDto;
import com.identlite.api.dto.AmenityRequestDto;
import com.identlite.api.models.Amenity;
import java.util.List;
import java.util.Optional;


public interface AmenityService {
    Amenity createAmenity(Amenity amenity);

    Amenity updateAmenity(Long id, AmenityRequestDto amenity);

    void deleteAmenity(Long id);

    Optional<Amenity> getAmenityById(Long id);

    List<Amenity> getAllAmenities();

    Optional<Amenity> getAmenityByName(String name);
}
