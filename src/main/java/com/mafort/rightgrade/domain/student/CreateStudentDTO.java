package com.mafort.rightgrade.domain.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateStudentDTO(
        @NotBlank(message = "name is mandatory")
        @Size(min = 2, max = 100, message = "Student name must be between 2 and 100 characters")
        String name,
        @NotNull UUID groupId
) {
        public CreateStudentDTO(Student student){
                this(student.getName(), student.getGroup().getId());
        }
}
