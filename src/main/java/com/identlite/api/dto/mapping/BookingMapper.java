package com.identlite.api.dto.mapping;

import com.identlite.api.dto.BookingDto;
import com.identlite.api.model.Booking;
import java.util.List;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {HotelMapper.class})
public interface BookingMapper {

    BookingDto toDto(Booking booking);

    List<BookingDto> toDto(List<Booking> bookings);

    Booking toEntity(BookingDto bookingDto);
}

