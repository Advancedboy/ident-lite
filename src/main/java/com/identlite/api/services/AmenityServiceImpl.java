package com.identlite.api.services;

import com.identlite.api.dto.AmenityRequestDto;
import com.identlite.api.models.Amenity;
import com.identlite.api.repositories.AmenityRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public Amenity createAmenity(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    @Override
    public Amenity updateAmenity(Long id, AmenityRequestDto amenity) {
        return amenityRepository.findById(id)
                .map(existing -> {
                    existing.setName(amenity.getName());
                    return amenityRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Amenity not found"));
    }

    @Override
    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }

    @Override
    public Optional<Amenity> getAmenityById(Long id) {
        return amenityRepository.findById(id);
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    public Optional<Amenity> getAmenityByName(String name) {
        return amenityRepository.findByName(name);
    }
}

