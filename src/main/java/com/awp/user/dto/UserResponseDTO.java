package com.awp.user.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserResponseDTO(

        Long id,
        String name,
        String password,
        String email,
        String phone,
        String address,
        Instant createdAt,
        Instant updatedAt,
        Boolean active
) {
}
