package com.sf.appUser.mapper;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    AppUser toEntity(AppUserRequest request);

    AppUserResponse toResponse(AppUser appUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateAppUser(AppUserRequest request, @MappingTarget AppUser appUser);

}
