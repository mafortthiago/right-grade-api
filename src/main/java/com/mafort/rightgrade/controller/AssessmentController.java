package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.assessment.Assessment;
import com.mafort.rightgrade.domain.assessment.AssessmentService;
import com.mafort.rightgrade.domain.assessment.CreateAssessment;
import com.mafort.rightgrade.domain.assessment.AssessmentResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("assessments")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentResponse> create(@Valid @RequestBody CreateAssessment createAssessment, UriComponentsBuilder uriComponentsBuilder){
        Assessment assessment = this.assessmentService.create(createAssessment);
        URI uri = uriComponentsBuilder.path("assessment/{id}").buildAndExpand(assessment.getId()).toUri();
        return ResponseEntity.created(uri).body(new AssessmentResponse(assessment));
    }

}
