package com.sf.appUser.dto;

import jakarta.persistence.Column;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppUserResponse(

        Long id,
        String fullName,
        String password,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean active
) {
}
