package com.identlite.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingDto {

    @NotNull(message = "User must not be null")
    @Valid
    private UserDto user;

    @NotNull(message = "Hotel must not be null")
    @Valid
    private HotelDto hotel;

    @NotNull(message = "Start date must not be null")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;
}
