package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.student.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.UUID;


@RequestMapping("students")
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody @Valid CreateStudentDTO createStudentDTO,
    UriComponentsBuilder uriComponentsBuilder){
        Student student = this.studentService.create(createStudentDTO);
        var uri = uriComponentsBuilder.path("groups/{id}").buildAndExpand(student.getId()).toUri();
        return ResponseEntity.created(uri).body(new StudentResponse(student));
    }

    @GetMapping("byGroup/{id}")
    public ResponseEntity<List<StudentListResponse>> getAll(@PathVariable UUID id) {
        List<StudentListResponse> studentsTableRow = studentService.getStudentsByGroup(id);
        return ResponseEntity.ok(studentsTableRow);
    }
}
