package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.assessment.*;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.AdvanceRequestDTO;
import uk.ac.sheffield.team_project_team_24.dto.CreateAssessmentDTO;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.AttachmentService;
import uk.ac.sheffield.team_project_team_24.service.CsvService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AssessmentController {

    @Autowired
    private final AssessmentService assessmentService;
    @Autowired
    private CsvService csvService;
    @Autowired
    private AttachmentService attachmentService;


    // Create an assessment
    @PostMapping("/assessments")
    public Assessment create(@RequestBody CreateAssessmentDTO req) {
        return assessmentService.createAssessment(req);
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

    // Upload csv file
    @PostMapping("/assessments/uploadCsv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        List<Assessment> assessments = csvService.parse(file);
        assessmentService.saveAll(assessments);
        return ResponseEntity.ok(assessments);
    }

    @PostMapping("/assessments/uploadAttachment")
    public ResponseEntity<?> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assessmentId") Long assessmentId
    ) {
        try {
            String path = attachmentService.saveAttachment(file, assessmentId);
            return ResponseEntity.ok("Uploaded successfully: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

}
