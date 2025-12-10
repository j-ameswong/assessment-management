package uk.ac.sheffield.team_project_team_24.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.EditModuleDTO;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleStaffRepository moduleStaffRepository;
    private final UserRepository userRepository;

    private static final String MODULE_NOT_FOUND = "Module does not exist";

    public Module createModule(Module newModule) {
        return moduleRepository.save(newModule);
    }

    public Module getModule(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    public List<Module> getModules() {
        return moduleRepository.findAll();
    }

    public Module getModule(String moduleCode) {
        return moduleRepository.findByModuleCode(moduleCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MODULE_NOT_FOUND));
    }

    public void deleteModule(String moduleCode) {
        Module module = moduleRepository.findByModuleCode(moduleCode)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        moduleRepository.delete(module);
    }

    // CreateModuleDTO
    public Module createModule(CreateModuleDTO dto) {

        if (dto.getModuleCode() == null || dto.getModuleCode().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module code is mandatory");
        }
        if (dto.getModuleName() == null || dto.getModuleName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module name is mandatory");
        }

        // Save module name and code
        Module module = new Module(dto.getModuleCode(), dto.getModuleName());
        module.setModuleStaff(new ArrayList<>());
        module = moduleRepository.save(module);

        List<ModuleStaff> staffEntries = new ArrayList<>();
        Set<Long> userIdList = new HashSet<>();

        // Module lead logic
        if (dto.getModuleLeadId() != null) {
            Long id = dto.getModuleLeadId();
            User leadUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found: " + id));

            staffEntries.add(new ModuleStaff(module, leadUser, ModuleRole.MODULE_LEAD));
            userIdList.add(id);
        }

        // Module moderator logic
        if (dto.getModuleModeratorId() != null) {
            Long id = dto.getModuleModeratorId();
            if (!userIdList.contains(id)) {
                User moderatorUser = userRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found: " + id));

                staffEntries.add(new ModuleStaff(module, moderatorUser, ModuleRole.MODERATOR));
                userIdList.add(id);
            }
        }

        // Module staff logic
        if (dto.getStaffIds() != null) {
            for (Long id : dto.getStaffIds()) {

                if (id == null) continue;
                if (userIdList.contains(id)) continue;

                User staffUser = userRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found: " + id));

                staffEntries.add(new ModuleStaff(module, staffUser, ModuleRole.STAFF));
                userIdList.add(id);
            }
        }

        module.setModuleStaff(staffEntries);
        return module;
    }

    public Module editModule(EditModuleDTO dto){
        System.out.println("EditModuleDTO: " + dto);

        if (dto.getOldModuleCode() == null || dto.getOldModuleCode().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old Module code is mandatory");
        }

        if (dto.getNewModuleCode() == null || dto.getNewModuleCode().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Module code is mandatory");
        }

        Module module = getModule(dto.getOldModuleCode());

        if (!dto.getOldModuleCode().equals(dto.getNewModuleCode())
            && moduleRepository.findByModuleCode(dto.getNewModuleCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Module code already in use");
        }

        module.setModuleCode(dto.getNewModuleCode());
        module.setModuleName(dto.getModuleName());

        List<ModuleStaff> currentStaff = module.getModuleStaff();

        if (currentStaff == null) {
            currentStaff = new ArrayList<>();
            module.setModuleStaff(currentStaff);
        } else {
            currentStaff.clear();
        }

        List<ModuleStaff> staffEntries = new ArrayList<>();
        Set<Long> userIdList = new HashSet<>();

        // Module lead logic
        if (dto.getModuleLeadId() != null) {
            Long id = dto.getModuleLeadId();
            User leadUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found: " + id));

            staffEntries.add(new ModuleStaff(module, leadUser, ModuleRole.MODULE_LEAD));
            userIdList.add(id);
        }

        // Module moderator logic
        if (dto.getModuleModeratorId() != null) {
            Long id = dto.getModuleModeratorId();
            if (!userIdList.contains(id)) {
                User moderatorUser = userRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found: " + id));

                staffEntries.add(new ModuleStaff(module, moderatorUser, ModuleRole.MODERATOR));
                userIdList.add(id);
            }
        }

        // Module staff logic
        if (dto.getStaffIds() != null) {
            for (Long id : dto.getStaffIds()) {

                if (id == null) continue;
                if (userIdList.contains(id)) continue;

                User staffUser = userRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found: " + id));

                staffEntries.add(new ModuleStaff(module, staffUser, ModuleRole.STAFF));
                userIdList.add(id);
            }
        }

        currentStaff.addAll(staffEntries);

        return moduleRepository.save(module);
    }
}
