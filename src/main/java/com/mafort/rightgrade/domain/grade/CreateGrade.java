package com.mafort.rightgrade.domain.grade;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateGrade(
        @NotNull
        UUID studentId,
        @NotNull
        UUID assessmentId,
        @DecimalMin(value = "0.0", inclusive = true, message = "Grade value must be at least 0")
        Double value
) {
}
