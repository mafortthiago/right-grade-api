package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.assessment.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/byGradingPeriod/{gradingPeriodId}")
    public ResponseEntity<List<AssessmentResponse>> getByGradingPeriodId(@PathVariable UUID gradingPeriodId){
        List<AssessmentResponse> assessments = this.assessmentService.getByGradingPeriodId(gradingPeriodId);
        return ResponseEntity.ok(assessments);
    }

    @PostMapping("/recovery")
    public ResponseEntity<RecoveryAssessmentResponse> createRecovery(@Valid @RequestBody CreateRecoveryAssessment createRecoveryAssessment, UriComponentsBuilder uriComponentsBuilder){
        RecoveryAssessmentResponse recoveryAssessmentResponse = this.assessmentService.createRecovery(createRecoveryAssessment);
        URI uri = uriComponentsBuilder.path("assessment/recovery/{id}").buildAndExpand(recoveryAssessmentResponse.id()).toUri();
        return ResponseEntity.created(uri).body(recoveryAssessmentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable UUID id){
        this.assessmentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
