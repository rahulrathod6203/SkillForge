package com.awp.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.Set;

@Builder
public record UserSummaryDTO(

        Long id,
        String name,
        String email,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Set<String> roles) {
}
