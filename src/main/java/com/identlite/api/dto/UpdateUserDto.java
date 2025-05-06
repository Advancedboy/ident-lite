package com.identlite.api.dto;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String name;
    private String email;
    private String password;
}