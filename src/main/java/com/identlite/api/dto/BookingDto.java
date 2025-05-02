package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private HotelDto hotel;
}