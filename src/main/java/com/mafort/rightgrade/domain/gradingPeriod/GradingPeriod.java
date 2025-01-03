package com.mafort.rightgrade.domain.gradingPeriod;

import com.mafort.rightgrade.domain.group.Group;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GradingPeriod {
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Group group;
    private LocalDateTime createdAt;

    public GradingPeriod(CreateGradingPeriod gradingPeriod, Group group){
        this.name = gradingPeriod.name();
        this.group = group;
        this.createdAt = LocalDateTime.now();
    }
}
