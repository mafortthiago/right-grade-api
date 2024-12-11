package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.page.CustomPage;
import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService {

    private final TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;

    public GroupService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Group createGroup(CreateGroupDTO createGroupDTO) {
        Teacher teacher = teacherRepository.findById(createGroupDTO.teacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return new Group(createGroupDTO, teacher);
    }
    public CustomPage<GroupListResponseDTO> findAllById(UUID id, Pageable pageable){
        Page<Group> pages = groupRepository.findByTeacherId(id, pageable);
        CustomPage<Group> customPages = new CustomPage<>(pages);
        return customPages.map(p ->new GroupListResponseDTO(p.getId(), p.getName(), p.getMinimumGrade(), p.isGradeFrom0To100()));
    }
}
