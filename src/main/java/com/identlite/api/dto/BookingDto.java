package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private HotelDto hotel;
    private LocalDate startDate;
    private LocalDate endDate;
}
