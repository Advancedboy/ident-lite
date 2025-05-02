package com.identlite.api.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private List<UserBookingDto> bookings;

    @Data
    public static class UserBookingDto {
        private LocalDate startDate;
        private LocalDate endDate;
        private BookingHotelDto hotel;
    }

    @Data
    public static class BookingHotelDto {
        private String name;
        private String address;
        private String city;
        private String description;
    }
}