package com.fitnessApp.userservice.dto;

import com.fitnessApp.userservice.model.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6, message = "Password must have at least 6 characters")
    private String password;

    @NotBlank(message = "You should confirm your password")
    private String confirmPassword;

    @NotBlank(message = "first name is required")
    private String firstName;

    private UserRole role=UserRole.USER;

    private String lastName;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
