package com.mafort.rightgrade.domain.authentication;

import com.mafort.rightgrade.domain.teacher.Teacher;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String token;
    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    public RefreshToken(String token, Teacher teacher){
        this.token = token;
        this.teacher = teacher;
    }
}
