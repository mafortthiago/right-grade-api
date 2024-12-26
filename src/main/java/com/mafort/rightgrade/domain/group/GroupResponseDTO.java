package com.mafort.rightgrade.domain.group;

import java.time.LocalDateTime;
import java.util.UUID;

public record GroupResponseDTO(UUID id, String name, LocalDateTime createdAt, boolean isGradeFrom0To100) {
    public  GroupResponseDTO(Group group){
        this(group.getId(), group.getName(), group.getCreatedAt(), group.isGradeFrom0To100());
    }
}
