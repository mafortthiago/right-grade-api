package com.mafort.rightgrade.domain.assessment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAssessment(
        @NotBlank(message = "Assessment name is mandatory")
        @Size(min = 2, max = 20, message = "Assessment name must be between 2 and 20 characters")
        String name,
        @NotNull(message = "Assessment value is mandatory")
        double value,
        @NotNull(message = "Grading period ID is mandatory")
        UUID gradingPeriodId) {
}
