package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.page.CustomPage;
import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import com.mafort.rightgrade.infra.exception.InvalidArgumentException;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.InvalidPropertiesFormatException;
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

    public CustomPage<GroupListResponseDTO> findAllById(UUID id, Pageable pageable, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Group> pages = groupRepository.findByTeacherId(id, sortedPageable);
        CustomPage<Group> customPages = new CustomPage<>(pages);
        return customPages.map(p -> new GroupListResponseDTO(p.getId(), p.getName(), p.getMinimumGrade(), p.isGradeFrom0To100()));
    }

    public GroupResponseDTO findById(UUID id) {
        if(this.groupRepository.findById(id).isEmpty()){
            throw new NotFoundException("Group with this ID does not exist");
        }
        return new GroupResponseDTO(this.groupRepository.findById(id).get());
    }
}