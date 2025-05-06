package com.identlite.api.dto.mapping;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.model.Booking;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(Booking booking);

    List<BookingDto> toDto(List<Booking> bookings);


    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "user", ignore = true)
    Booking toEntity(BookingDto bookingDto);
}

