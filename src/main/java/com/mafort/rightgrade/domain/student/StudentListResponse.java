package com.mafort.rightgrade.domain.student;

import com.mafort.rightgrade.domain.grade.GradeResponse;

import java.util.List;
import java.util.UUID;

public record StudentListResponse(
        UUID id,
        String name,
        UUID groupId,
        List<GradeResponse> grades
) {
}
