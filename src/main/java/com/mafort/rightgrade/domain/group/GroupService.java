package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final TeacherRepository teacherRepository;

    public GroupService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Group createGroup(CreateGroupDTO createGroupDTO) {
        Teacher teacher = teacherRepository.findById(createGroupDTO.teacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return new Group(createGroupDTO, teacher);
    }
}
