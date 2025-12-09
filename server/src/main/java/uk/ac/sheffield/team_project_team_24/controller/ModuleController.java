package uk.ac.sheffield.team_project_team_24.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleStaffDTO;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleStaffService moduleStaffService;

    // Create module
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modules")
    public ResponseEntity<ModuleDTO> createModule(@RequestBody CreateModuleDTO moduleDTO) {
        Module module = moduleService.createModule(moduleDTO);
        return ResponseEntity.ok(ModuleDTO.fromEntity(module));
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
                .map(ModuleDTO::fromEntity)
                .toList());
    }

    // Assign staff to a module
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<List<ModuleStaffDTO>> listStaff(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleStaffService.getAllModuleStaffInModule(moduleId)
                .stream()
                .map(ModuleStaffDTO::fromEntity)
                .toList());
    }

    // Delete Module
    @DeleteMapping("/modules/delete/{moduleCode}")
    public ResponseEntity<Void> deleteModule(@PathVariable String moduleCode) {
        moduleService.deleteModule(moduleCode);
        return ResponseEntity.ok().build();
    }

}
