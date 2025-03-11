package com.mafort.rightgrade.domain.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeacherUpdateRequest(
        @NotBlank
        @NotBlank(message = "Name is mandatory")
        @Size(min=2, max=100, message = "The name should be between 2 and 100 characters.")
        String name
) {
}
