package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.student.CreateStudentDTO;
import com.mafort.rightgrade.domain.student.Student;
import com.mafort.rightgrade.domain.student.StudentResponse;
import com.mafort.rightgrade.domain.student.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;


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

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll(){
        List<Student> students = this.studentService.getAll();
        return ResponseEntity.ok(students.stream().map(StudentResponse::new).toList());
    }
}
