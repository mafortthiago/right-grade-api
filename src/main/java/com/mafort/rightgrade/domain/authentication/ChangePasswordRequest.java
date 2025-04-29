package com.mafort.rightgrade.domain.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.valid}")
        String email,
        @NotBlank(message = "{password.required}")
        @Size(min = 8, message = "{password.size}")
        String newPassword,
        @NotBlank(message = "{code.required}")
        String code) {
}
