package com.mafort.rightgrade.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("""
    SELECT s.id, s.name, g.id, gr.value, gr.id, a.id
    FROM Student s
    JOIN Group g ON g.id = s.group.id
    LEFT JOIN Grade gr ON gr.student.id = s.id
    LEFT JOIN Assessment a ON a.id = gr.assessment.id
    WHERE g.id = :groupId
    """)
    List<Object[]> findStudentsByGroupId(@Param("groupId") UUID groupId);

    @Query("""
    SELECT COUNT(s.id)
    FROM Student s
    JOIN Group g ON g.id = s.group.id
    WHERE g.id = :groupId
    """)
    int getStudentsByGroupId(@Param("groupId") UUID groupId);

}
