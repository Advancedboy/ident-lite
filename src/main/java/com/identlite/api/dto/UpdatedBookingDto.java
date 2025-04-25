package com.identlite.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedBookingDto {
    private Long id;
    private LocalDate checkInDate;  // Дата заезда (можно обновить)
    private LocalDate checkOutDate; // Дата выезда (можно обновить)
    private RoomDto room;     // Комната (можно обновить)
}

