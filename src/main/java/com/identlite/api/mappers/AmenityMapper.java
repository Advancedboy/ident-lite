package com.identlite.api.mappers;

import com.identlite.api.dto.AmenityDto;
import com.identlite.api.dto.AmenityRequestDto;
import com.identlite.api.models.Amenity;
import org.springframework.stereotype.Component;

@Component
public class AmenityMapper {

    public static Amenity toEntity(AmenityRequestDto dto) {
        return Amenity.builder()
                .name(dto.getName())
                .build();
    }

    public static AmenityDto toDto(Amenity amenity) {
        return AmenityDto.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .build();
    }
}