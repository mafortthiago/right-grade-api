package com.mafort.rightgrade.domain.group;

import java.util.UUID;

public record GroupListResponseDTO(
        UUID id,
        String name,
        double minimumGrade,
        boolean isGradeFrom0To100
) {
}
