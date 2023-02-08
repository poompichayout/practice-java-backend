package com.poompich.training.backend.mapper;

import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.model.MRegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    MRegisterResponse toRegisterResponse(User user);
}
