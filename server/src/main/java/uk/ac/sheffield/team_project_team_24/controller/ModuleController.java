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
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.EditModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleStaffDTO;

import uk.ac.sheffield.team_project_team_24.service.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleStaffService moduleStaffService;
    private final ModuleCsvService moduleCsvService;

    // Create module
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modules")
    public ResponseEntity<ModuleDTO> createModule(@RequestBody CreateModuleDTO moduleDTO) {
        Module module = moduleService.createModule(moduleDTO);
        return ResponseEntity.ok(ModuleDTO.fromEntity(module));
    }

    // Upload csv file
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modules/uploadCsv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        List<Module> modules = moduleCsvService.parse(file);
        moduleService.saveAll(modules);
        return ResponseEntity.ok(modules);
    }

    // Get one module
    @GetMapping("/modules/{id}")
    public ResponseEntity<ModuleDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(ModuleDTO.fromEntity(moduleService.getModule(id)));
    }

    @GetMapping("/modules/edit/{moduleCode}")
    public ResponseEntity<ModuleDTO> get(@PathVariable String moduleCode) {
        return ResponseEntity.ok(ModuleDTO.fromEntity(moduleService.getModule(moduleCode)));
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

    // Sets module as inactive
    @DeleteMapping("/modules/delete/{moduleCode}")
    public ResponseEntity<Void> deleteModule(@PathVariable String moduleCode) {
        moduleService.deleteModule(moduleCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modules/edit")
    public ResponseEntity<ModuleDTO> editModule(@RequestBody EditModuleDTO moduleDTO) {
        Module module = moduleService.editModule(moduleDTO);
        return ResponseEntity.ok(ModuleDTO.fromEntity(module));
    }

}
