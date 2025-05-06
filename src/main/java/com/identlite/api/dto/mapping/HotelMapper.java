package com.identlite.api.dto.mapping;

import com.identlite.api.dto.HotelDto;
import com.identlite.api.model.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface HotelMapper {
    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto dto);
}
