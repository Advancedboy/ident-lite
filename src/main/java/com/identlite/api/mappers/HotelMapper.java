package com.identlite.api.mappers;

import com.identlite.api.dto.AmenityDto;
import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.RoomDto;
import com.identlite.api.models.Amenity;
import com.identlite.api.models.Hotel;
import com.identlite.api.models.Room;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public HotelDto toDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .description(hotel.getDescription())
                .rooms(hotel.getRooms().stream()
                        .map(this::toRoomDto)
                        .toList())
                .amenities(hotel.getAmenities().stream()
                        .map(this::toAmenityDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    private RoomDto toRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .type(room.getType())
                .price(room.getPrice())
                .build();
    }

    private AmenityDto toAmenityDto(Amenity amenity) {
        return AmenityDto.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .build();
    }
}

