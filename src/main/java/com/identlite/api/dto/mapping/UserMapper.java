package com.identlite.api.dto.mapping;

import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Booking;
import com.identlite.api.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface UserMapper {

    @Mapping(target = "bookings", source = "bookings")
    UserDto toDto(User user);

    UserDto.UserBookingDto toUserBookingDto(Booking booking);

    default List<UserDto.UserBookingDto> mapBookings(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toUserBookingDto)
                .toList();
    }
}