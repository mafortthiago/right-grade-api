package com.mafort.rightgrade.domain.gradingPeriod;

import com.mafort.rightgrade.domain.assessment.Assessment;
import com.mafort.rightgrade.domain.group.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GradingPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Group group;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "gradingPeriod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assessment> assessments;

    public GradingPeriod(CreateGradingPeriod gradingPeriod, Group group){
        this.name = gradingPeriod.name();
        this.group = group;
        this.createdAt = LocalDateTime.now();
    }
}
