package com.identlite.api.controller;

import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.mapping.HotelMapper;
import com.identlite.api.model.Hotel;
import com.identlite.api.service.HotelService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotelDtos = hotelService.getAllHotels().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotelMapper.toDto(hotel));
    }

    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@RequestBody HotelDto hotelDto) {
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        Hotel saved = hotelService.createHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long id,
                                                @RequestBody HotelDto hotelDto) {
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        Hotel updated = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(hotelMapper.toDto(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HotelDto> partialUpdateHotel(@PathVariable Long id,
                                                       @RequestBody HotelDto hotelDto) {
        Hotel existingHotel = hotelService.getHotelById(id);

        if (hotelDto.getName() != null) {
            existingHotel.setName(hotelDto.getName());
        }
        if (hotelDto.getAddress() != null) {
            existingHotel.setAddress(hotelDto.getAddress());
        }
        if (hotelDto.getCity() != null) {
            existingHotel.setCity(hotelDto.getCity());
        }
        if (hotelDto.getDescription() != null) {
            existingHotel.setDescription(hotelDto.getDescription());
        }

        Hotel updated = hotelService.updateHotel(id, existingHotel);
        return ResponseEntity.ok(hotelMapper.toDto(updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
