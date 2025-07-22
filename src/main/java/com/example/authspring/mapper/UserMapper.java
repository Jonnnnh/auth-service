package com.example.authspring.mapper;

import com.example.authspring.dto.response.UserDto;
import com.example.authspring.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
