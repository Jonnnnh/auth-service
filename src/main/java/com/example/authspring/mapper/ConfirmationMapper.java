package com.example.authspring.mapper;

import com.example.authspring.dto.response.ConfirmationDto;
import com.example.authspring.model.Confirmation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfirmationMapper {
    @Mapping(source = "user.id", target = "userId")
    ConfirmationDto toDto(Confirmation confirmation);

    @Mapping(target = "user", ignore = true)
    Confirmation toEntity(ConfirmationDto dto);
}