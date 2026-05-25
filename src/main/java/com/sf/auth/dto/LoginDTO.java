package com.sf.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginDTO(

        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Enter a valid email!")
        String email,

        @NotBlank(message = "Password cannot be blank!")
        @Size(min = 8, message = "Invalid password!")
        String password
) {
}
