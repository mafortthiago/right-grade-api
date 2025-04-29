package com.mafort.rightgrade.domain.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyCodeRequest(
        @Email(message = "{error.invalid.email}")
        @NotBlank(message = "{error.required.email}")
        String email,

        @NotBlank(message = "{error.required.code}")
        @Size(min = 6, max = 6, message = "{error.invalid.length}")
        String code
) {
}