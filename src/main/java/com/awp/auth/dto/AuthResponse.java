package com.awp.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.Instant;

@Builder
public record AuthResponse(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String tokenType, // Will always be "Bearer"

        Instant timestamp,
        String message,
        UserSummaryDTO user) {
}
