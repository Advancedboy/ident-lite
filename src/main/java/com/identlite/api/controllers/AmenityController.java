package com.identlite.api.controllers;

import com.identlite.api.dto.AmenityDto;
import com.identlite.api.dto.AmenityRequestDto;
import com.identlite.api.mappers.AmenityMapper;
import com.identlite.api.models.Amenity;
import com.identlite.api.services.AmenityService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;
    private final AmenityMapper amenityMapper;

    public AmenityController(AmenityService amenityService, AmenityMapper amenityMapper) {
        this.amenityService = amenityService;
        this.amenityMapper = amenityMapper;
    }

    @PostMapping
    public ResponseEntity<AmenityDto> createAmenity(@RequestBody AmenityRequestDto dto) {
        Amenity entity = AmenityMapper.toEntity(dto);
        Amenity saved = amenityService.createAmenity(entity);
        return ResponseEntity.ok(AmenityMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDto> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id)
                .map(AmenityMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AmenityDto>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        List<AmenityDto> amenityDtos = amenities.stream()
                .map(AmenityMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(amenityDtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AmenityDto> updateAmenity(@PathVariable Long id,
                                                    @RequestBody AmenityRequestDto amenityDto) {
        Amenity updatedAmenity = amenityService.updateAmenity(id, amenityDto);
        AmenityDto responseDto = AmenityMapper.toDto(updatedAmenity);
        return ResponseEntity.ok(responseDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<AmenityDto> getAmenityByName(@RequestParam("name") String name) {
        return amenityService.getAmenityByName(name)
                .map(AmenityMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}