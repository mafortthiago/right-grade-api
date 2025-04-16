package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.domain.teacher.Teacher;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "class")
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @Column(name = "gradeType")
    private boolean isGradeFrom0To100;
    private LocalDateTime createdAt;
    @Setter
    private double minimumGrade;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<GradingPeriod> gradingPeriods;

    public Group(CreateGroupDTO createGroupDTO, Teacher teacher){
        this.name = createGroupDTO.name();
        this.isGradeFrom0To100 = createGroupDTO.gradeType();
        this.createdAt = LocalDateTime.now();
        this.teacher = teacher;
        this.minimumGrade = createGroupDTO.minimumGrade();
    }

    public void validate(MessageSource messageSource){
        if(this.name.length() < 2 || this.name.length() > 20){
            throw new InvalidArgumentException(messageSource.getMessage("error.class.nameSize", null, LocaleContextHolder.getLocale()));
        }
        if(this.minimumGrade < 1 || this.minimumGrade > 100){
            throw new InvalidArgumentException(messageSource.getMessage("error.class.minimumGrade", null, LocaleContextHolder.getLocale()));

        }
    }
}
