package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import uk.ac.sheffield.team_project_team_24.domain.assessment.*;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.AdvanceRequestDTO;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    // Create an assessment
    @PostMapping("/assessments")
    public ResponseEntity<AssessmentDTO> create(@RequestBody AssessmentDTO dto) {
        Assessment created = assessmentService.createAssessment(dto.toEntity());
        return ResponseEntity.ok(AssessmentDTO.fromEntity(created));
    }

    // Get one assessment
    @GetMapping("/assessments/{id}")
    public ResponseEntity<AssessmentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(
                AssessmentDTO.fromEntity(assessmentService.getAssessment(id)));
    }

    // Get all assessments
    @GetMapping("/assessments")
    public ResponseEntity<List<AssessmentDTO>> list() {
        return ResponseEntity.ok(
                assessmentService.getAllAssessments().stream()
                        .map(AssessmentDTO::fromEntity)
                        .toList());
    }

    // Update assessment
    @PutMapping("/assessments/{id}")
    public ResponseEntity<AssessmentDTO> update(
            @PathVariable Long id,
            @RequestBody AssessmentDTO dto) {

        Assessment updated = assessmentService.updateAssessment(id, dto);
        return ResponseEntity.ok(AssessmentDTO.fromEntity(updated));
    }

    // Delete assessment
    @DeleteMapping("/assessments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }

    // Advance the assessment process
    @PostMapping("/assessments/{id}/advance")
    public ResponseEntity<AssessmentDTO> advance(
            @PathVariable Long id,
            @RequestBody AdvanceRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Assessment updated = assessmentService.advanceStage(
                id,
                request.getAssessmentStage(),
                currentUser.getId(),
                request.getNote()

        );

        return ResponseEntity.ok(AssessmentDTO.fromEntity(updated));
    }

    // View process history
    @GetMapping("/assessments/{id}/history")
    public ResponseEntity<List<AssessmentStageLog>> history(@PathVariable Long id) {

        return ResponseEntity.ok(assessmentService.getHistory(id));
    }
}
