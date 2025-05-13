package com.identlite.api.dto;

import com.identlite.api.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;
import lombok.Data;

@Data
@ValidDateRange
public class UpdateBookingDto {

    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;
}
