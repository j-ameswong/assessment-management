package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleStaffService moduleStaffService;

    // Create module
    // TODO: use module DTOs, only authorize admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modules")
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        return ResponseEntity.ok(moduleService.createModule(module));
    }

    // Get one module
    @GetMapping("/modules/{id}")
    public ResponseEntity<Module> get(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.getModule(id));
    }

    // List modules
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> list() {
        return ResponseEntity.ok(moduleService.getModules());
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

}
