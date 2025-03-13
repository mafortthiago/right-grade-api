package com.mafort.rightgrade.domain.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordValidationRequest(
        @NotBlank
        @Size(min=8)
        String password,
        @Email
        String email
) {
}
