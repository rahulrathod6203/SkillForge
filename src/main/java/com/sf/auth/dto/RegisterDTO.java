package com.sf.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDTO(

        @NotBlank(message = "Name cannot be blank!")
        @Size(min = 3, message = "Name should have minimum 3 characters!")
        String fullName,

        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Enter a valid email!")
        String email,

        @NotBlank(message = "Password cannot be blank!")
        @Size(min = 8, message = "Password should have minimum 8 characters!")
        String password,

        @NotBlank(message = "Phone cannot be blank!")
        @Size(min = 10, message = "Enter a valid phone number!")
        String phone,

        @NotBlank(message = "Address cannot be blank!")
        String address

) {
}
