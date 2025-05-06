package com.identlite.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<BookingDto> bookings;

    public UserDto()
    {}

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
