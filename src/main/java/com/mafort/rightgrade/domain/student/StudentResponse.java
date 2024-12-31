package com.mafort.rightgrade.domain.student;

import java.util.UUID;

public record StudentResponse(UUID id, String name) {
    public StudentResponse(Student student){
        this(student.getId(), student.getName());
    }
}
