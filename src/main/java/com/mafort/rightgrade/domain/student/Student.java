package com.mafort.rightgrade.domain.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mafort.rightgrade.domain.group.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "student")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name="group_id")
    @JsonIgnore
    private Group group;

    public Student(CreateStudentDTO studentDTO, Group group){
        this.name = studentDTO.name();
        this.group = group;
    }
}
