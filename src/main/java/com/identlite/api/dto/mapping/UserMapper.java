package com.identlite.api.dto.mapping;

import com.identlite.api.dto.UserDto;
import com.identlite.api.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface UserMapper {

    @Mapping(source = "bookings", target = "bookings")
    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    User toEntity(UserDto dto);
}

