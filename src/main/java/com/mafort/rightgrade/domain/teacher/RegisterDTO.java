package com.mafort.rightgrade.domain.teacher;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "{error.authentication.name}")
        @Size(min = 2, max = 100, message = "{error.authentication.nameSize}")
        String name,
        @NotBlank(message = "{error.authentication.email}")
        @Email(message = "{error.authentication.validEmail}")
        String email,
        @NotBlank(message = "{error.authentication.password}")
        @Size(min = 8, message = "{error.authentication.passwordSize}")
        String password,
        @AssertTrue(message = "{error.authentication.acceptPolicy}")
        boolean acceptPolicy
) {
}
