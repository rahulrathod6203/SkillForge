package com.awp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record UserResponseDTO(

        Long id,
        String name,
        String email,
        String phone,
        String address,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm a", timezone = "Asia/Kolkata")
        Instant createdAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm a", timezone = "Asia/Kolkata")
        Instant updatedAt,

        Boolean active,
        Set<String> roles
) {
}
