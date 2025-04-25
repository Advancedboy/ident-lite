package com.identlite.api.dto;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequestDto {
    private String name;
    private String address;
    private String description;
    private List<Long> roomIds;       // или inline данные, если нужно
    private Set<Long> amenityIds;
}

