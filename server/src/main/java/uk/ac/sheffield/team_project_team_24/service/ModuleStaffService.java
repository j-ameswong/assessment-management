package uk.ac.sheffield.team_project_team_24.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaffId;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
public class ModuleStaffService {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private final ModuleStaffRepository moduleStaffRepository;
    @Autowired
    private UserRepository userRepository;

    public ModuleStaffService(ModuleStaffRepository moduleStaffRepository,
                              ModuleRepository moduleRepository,
                              UserRepository userRepository) {
        this.moduleStaffRepository = moduleStaffRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
    }

    public void assignModuleStaff(ModuleStaff moduleStaff) {
    moduleStaffRepository.save(moduleStaff);
    }

    public void assignModuleStaff(List<ModuleStaff> moduleStaff) {
    moduleStaffRepository.saveAll(moduleStaff);
    }

    public void assignUserToModule(Long moduleId, Long userId, ModuleRole role) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ModuleStaff ms = new ModuleStaff();
        ms.setModule(module);
        ms.setUser(user);
        ms.setModuleRole(role);
        ms.setId(new ModuleStaffId(moduleId, userId));

        moduleStaffRepository.save(ms);
    }

}


// TODO: ideas on what to implement:
// 1. get all users from a module by module id
// 2. get specific role of the module
// 3. get missing roles (no module lead, moderator, etc)
