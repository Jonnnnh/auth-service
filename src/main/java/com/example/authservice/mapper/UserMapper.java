package com.example.authservice.mapper;

import com.example.authservice.dto.response.UserDto;
import com.example.authservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "active", target = "isActive")
    UserDto toDto(User user);
}
