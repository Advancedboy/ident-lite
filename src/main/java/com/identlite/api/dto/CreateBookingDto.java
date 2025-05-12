package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingDto {
    private UserDto user;
    private HotelDto hotel;
    private LocalDate startDate;
    private LocalDate endDate;
}
