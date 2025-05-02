package com.identlite.api.dto.mapping;

import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelDto toDto(Hotel hotel);

    UserDto.BookingHotelDto toBookingHotelDto(Hotel hotel);
}