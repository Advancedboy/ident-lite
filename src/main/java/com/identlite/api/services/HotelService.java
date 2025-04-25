package com.identlite.api.services;


import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.HotelRequestDto;
import java.util.List;
import java.util.Optional;


public interface HotelService {
    HotelDto getHotelById(Long id);

    HotelDto createHotel(HotelRequestDto hotelDto);

    HotelDto updateHotel(Long id, HotelRequestDto hotelDto);

    boolean deleteHotel(Long id);

    List<HotelDto> getAllHotels();

    Optional<HotelDto> getHotelByName(String name);
}
