package com.mafort.rightgrade.domain.student;

import java.util.List;
import java.util.UUID;

public record StudentListResponse(
        UUID id,
        String name,
        UUID groupId,
        List<Double> grades
) {
}
