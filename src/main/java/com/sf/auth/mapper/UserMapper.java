package com.sf.auth.mapper;

import com.sf.user.dto.UserRequestDTO;
import com.sf.auth.dto.RegisterDTO;
import com.sf.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegisterDTO registerDTO);

}
