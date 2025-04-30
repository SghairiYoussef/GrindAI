package com.fitnessApp.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Should be a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
