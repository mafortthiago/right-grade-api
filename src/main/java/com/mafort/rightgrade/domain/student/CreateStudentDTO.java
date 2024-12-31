package com.mafort.rightgrade.domain.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStudentDTO(
        @NotBlank(message = "name is mandatory")
        @Size(min = 2, max = 25, message = "Student name must be between 2 and 25 characters")
        String name) {
        public CreateStudentDTO(Student student){
                this(student.getName());
        }
}
