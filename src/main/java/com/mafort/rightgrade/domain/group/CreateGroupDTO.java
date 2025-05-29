package com.mafort.rightgrade.domain.group;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateGroupDTO(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 100, message = "Class name must be between 2 and 100 characters.")
        String name,
        @NotNull(message = "Grade type is mandatory.")
        Boolean isGradeFrom0To100,
        @NotNull(message = "Teacher id is mandatory.")
        UUID teacherId,
        @NotNull(message = "Minimum grade is mandatory")
        @DecimalMin(value = "0.00", message = "Minimum grade must be positive.")
        @Digits(integer = 5, fraction = 2, message = "The minimum grade must be a valid number with up to 5 digits and 2 decimal places.")
        Double minimumGrade
) {
}
