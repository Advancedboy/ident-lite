package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
}
