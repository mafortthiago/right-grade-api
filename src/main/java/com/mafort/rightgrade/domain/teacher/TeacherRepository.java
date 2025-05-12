package com.mafort.rightgrade.domain.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    UserDetails findByEmail(String email);
    @Transactional
    @Modifying
    @Query("DELETE FROM Teacher t WHERE t.email = :email")
    void deleteTeacherByEmail(String email);
    Optional<Teacher> findById(UUID uuid);
}
