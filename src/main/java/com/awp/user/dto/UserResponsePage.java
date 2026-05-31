package com.awp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
public record UserResponsePage(

        List<UserResponseDTO> content,
        int pageNo,
        int pageSize,
        int totalPages,
        boolean lastPage
) {
}
