package com.identlite.api.dto.mapping;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HotelMapper.class, UserMapper.class})
public interface BookingMapper {

    @Mapping(target = "hotel", source = "hotel")
    BookingDto toDto(Booking booking);

    UserDto.UserBookingDto toUserBookingDto(Booking booking);
}
