package com.mafort.rightgrade.domain.gradingPeriod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditGradingPeriod(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
        String name)
{
}
