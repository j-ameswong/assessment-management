package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.assessment.*;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentOverviewDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentProgressDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentStageDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.AdvanceRequestDTO;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageService;
import uk.ac.sheffield.team_project_team_24.service.AttachmentService;
import uk.ac.sheffield.team_project_team_24.service.CsvService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;

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
    private final AssessmentStageService assessmentStageService;
    private final ModuleService moduleService;

    // Create an assessment
    @PostMapping("/assessments")
    public ResponseEntity<AssessmentDTO> create(@RequestBody AssessmentDTO req) {
        return ResponseEntity.ok(AssessmentDTO
                .fromEntity(assessmentService.createAssessment(req)));
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
            @RequestParam("assessmentId") Long assessmentId) {
        try {
            String path = attachmentService.saveAttachment(file, assessmentId);
            return ResponseEntity.ok("Uploaded successfully: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/modules/{moduleId}/assessments")
    public ResponseEntity<AssessmentOverviewDTO> getOverview(
            @PathVariable Long moduleId) {
        ModuleDTO moduleDTO = ModuleDTO.fromEntity(
                moduleService.getModule(moduleId));
        List<AssessmentDTO> assessmentDTOs = assessmentService.getAssessmentsInModule(moduleId)
                .stream()
                .map(a -> AssessmentDTO.fromEntity(a))
                .toList();
        List<AssessmentStageDTO> assessmentStageDTOs = assessmentStageService.getAllStages()
                .stream()
                .map(s -> AssessmentStageDTO.fromEntity(s))
                .toList();

        return ResponseEntity.ok(
                AssessmentOverviewDTO.combineEntities(moduleDTO, assessmentDTOs, assessmentStageDTOs));
    }

    @GetMapping("/assessments/stages")
    public ResponseEntity<List<AssessmentStageDTO>> listAllStages() {
        return ResponseEntity.ok(assessmentStageService.getAllStages()
                .stream()
                .map(s -> AssessmentStageDTO.fromEntity(s))
                .toList());
    }

    @GetMapping("/assessments/{type}/stages")
    public ResponseEntity<List<AssessmentStageDTO>> listStages(
            @PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentStageService.getAllStagesByType(type)
                .stream()
                .map(s -> AssessmentStageDTO.fromEntity(s))
                .toList());
    }

    @GetMapping("/assessments/stages/{id}")
    public ResponseEntity<AssessmentStageDTO> getStage(
            @PathVariable Long id) {
        return ResponseEntity.ok(AssessmentStageDTO.fromEntity(
                assessmentStageService.getAssessmentStage(id)));
    }

    @GetMapping("/assessments/{id}/progress")
    public ResponseEntity<AssessmentProgressDTO> getProgress(
            @PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getProgress(id));
    }
    // @GetMapping("/modules/{moduleId}/assessments")
    // public ResponseEntity<List<AssessmentDTO>> listAssessments(
    // @PathVariable Long moduleId) {
    // return ResponseEntity.ok(assessmentService.getAssessmentsInModule(moduleId)
    // .stream()
    // .map(a -> AssessmentDTO.fromEntity(a))
    // .toList());
    // }
}
