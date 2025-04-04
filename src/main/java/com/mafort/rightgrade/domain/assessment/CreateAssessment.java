package com.mafort.rightgrade.domain.assessment;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAssessment(
        @NotBlank(message = "Assessment name is mandatory")
        @Size(min = 2, max = 20, message = "Assessment name must be between 2 and 20 characters")
        String name,
        @NotNull(message = "Assessment value is mandatory")
        @Min(value = 1, message = "{error.minimumAssessmentValue}")
        double value,
        @NotNull(message = "Grading period ID is mandatory")
        UUID gradingPeriodId,
        boolean isRecovery
) {
}
