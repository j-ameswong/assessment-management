package uk.ac.sheffield.team_project_team_24.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleStaffDTO;
import uk.ac.sheffield.team_project_team_24.service.ModuleCsvService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;

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

    @PostMapping(
            path = "/modules/uploadCsv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ModuleDTO>> uploadModulesCsv(@RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(moduleCsvService.processCsv(file));
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
