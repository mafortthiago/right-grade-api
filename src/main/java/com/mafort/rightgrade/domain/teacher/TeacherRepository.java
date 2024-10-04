package com.mafort.rightgrade.domain.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    UserDetails findByEmail(String email);
}
