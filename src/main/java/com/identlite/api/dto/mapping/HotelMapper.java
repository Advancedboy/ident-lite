package com.identlite.api.dto.mapping;

import com.identlite.api.dto.HotelDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelDto toDto(Hotel hotel);

    UserDto.BookingHotelDto toBookingHotelDto(Hotel hotel);
}