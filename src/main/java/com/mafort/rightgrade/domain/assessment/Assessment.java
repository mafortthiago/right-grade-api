package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.infra.exception.InvalidArgumentException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Assessment implements AssessmentBase{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    @ManyToOne
    @JoinColumn(name = "grading_period_id")
    private GradingPeriod gradingPeriod;
    private LocalDateTime createdAt;
    @Setter
    private double value;
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    public Assessment(CreateAssessment createAssessment, GradingPeriod gradingPeriod){
        this.name = createAssessment.name();
        this.value = createAssessment.value();
        this.gradingPeriod = gradingPeriod;
        this.createdAt = LocalDateTime.now();
    }

    public void validate(MessageSource messageSource) {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new InvalidArgumentException(messageSource.getMessage("error.assessment.nameRequired", null, LocaleContextHolder.getLocale()));
        } else if (this.name.trim().length() < 2 || this.name.trim().length() > 20) {
            throw new InvalidArgumentException(messageSource.getMessage("error.assessment.nameSize", null, LocaleContextHolder.getLocale()));
        }
        if (this.value < 1) {
            throw new InvalidArgumentException(messageSource.getMessage("error.minimumAssessmentValue", null, LocaleContextHolder.getLocale()));
        }
    }
}
