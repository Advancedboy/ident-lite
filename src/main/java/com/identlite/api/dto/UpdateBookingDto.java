package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateBookingDto {
    private LocalDate startDate;
    private LocalDate endDate;
}

