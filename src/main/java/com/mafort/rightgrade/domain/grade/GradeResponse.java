package com.mafort.rightgrade.domain.grade;

import java.util.UUID;

public record GradeResponse(UUID id, UUID assessmentId, UUID studentId, double value) {
}
