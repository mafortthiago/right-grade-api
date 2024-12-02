package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private String name;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @Column(name = "gradeType")
    private boolean isGradeFrom0To100;
    private LocalDateTime createdAt;
    private double minimumGrade;

    public Group(CreateGroupDTO createGroupDTO, Teacher teacher){
        this.name = createGroupDTO.name();
        this.isGradeFrom0To100 = createGroupDTO.gradeType();
        this.createdAt = LocalDateTime.now();
        this.teacher = teacher;
        this.minimumGrade = createGroupDTO.minimumGrade();
    }
}
