package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentStageDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleStaffDTO;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleStaffService moduleStaffService;
    private final AssessmentService assessmentService;
    private final AssessmentStageService assessmentStageService;

    // Create module
    // TODO: use module DTOs, only authorize admins
    // TODO: only fetch modules that have the user/all for admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modules")
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        return ResponseEntity.ok(moduleService.createModule(module));
    }

    // Get one module
    @GetMapping("/modules/{id}")
    public ResponseEntity<ModuleDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(ModuleDTO.fromEntity(moduleService.getModule(id)));
    }

    // List modules
    @GetMapping("/modules")
    public ResponseEntity<List<ModuleDTO>> list() {
        return ResponseEntity.ok(moduleService.getModules()
                .stream()
                .map(m -> ModuleDTO.fromEntity(m))
                .toList());
    }

    // Assign staff to a module
    @PostMapping("/modules/{moduleId}/staff/{userId}")
    public ResponseEntity<Void> assign(
            @PathVariable Long moduleId,
            @PathVariable Long userId,
            @RequestParam ModuleRole role) {

        moduleStaffService.assignUserToModule(moduleId, userId, role);
        return ResponseEntity.ok().build();
    }

    // Get all staff in a module
    @GetMapping("/modules/{moduleId}/staff")
    public ResponseEntity<List<ModuleStaffDTO>> listStaff(
            @PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleStaffService.getAllModuleStaffInModule(moduleId)
                .stream()
                .map(ms -> ModuleStaffDTO.fromEntity(ms))
                .toList());
    }

    @GetMapping("/modules/{moduleId}/assessments")
    public ResponseEntity<List<AssessmentDTO>> listAssessments(
            @PathVariable Long moduleId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsInModule(moduleId)
                .stream()
                .map(a -> AssessmentDTO.fromEntity(a))
                .toList());
    }

    @GetMapping("/assessments/stages/{type}")
    public ResponseEntity<List<AssessmentStageDTO>> listStages(
            @PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentStageService.getAllStagesByType(type)
                .stream()
                .map(s -> AssessmentStageDTO.fromEntity(s))
                .toList());
    }
}
