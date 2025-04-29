package com.mafort.rightgrade.domain.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendCodeRequest(
        @Email(message = "{error.invalid.email}")
        @NotBlank(message = "{error.required.email}")
        String email
) {
}
