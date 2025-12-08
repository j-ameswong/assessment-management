package uk.ac.sheffield.team_project_team_24.service;

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
import uk.ac.sheffield.team_project_team_24.exception.user.UserNotFoundException;
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

    private final UserService userService;
    private final ModuleService moduleService;

    public ModuleStaffService(ModuleStaffRepository moduleStaffRepository,
            ModuleRepository moduleRepository,
            UserRepository userRepository,
            UserService userService,
            ModuleService moduleService) {
        this.moduleStaffRepository = moduleStaffRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.moduleService = moduleService;
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

    public List<ModuleStaff> getAllModuleStaffInModule(Long moduleId) {
        return moduleStaffRepository.findByModuleId(moduleId)
                .orElseThrow(() -> new UserNotFoundException("No users in module"));
    }

    public ModuleStaff getUserInModule(Long userId, Long moduleId) {
        return moduleStaffRepository.findByUserAndModule(
                userService.getUser(userId), moduleService.getModule(moduleId))
                .orElseThrow(() -> new UserNotFoundException("No such module staff exists"));
    }

    public User getUserByRole(Long moduleId, ModuleRole moduleRole) {
        Optional<ModuleStaff> moduleStaff = moduleStaffRepository
                .findFirstByModuleRoleAndModuleId(moduleRole, moduleId);
        if (moduleStaff.isPresent()) {
            return moduleStaff.get()
                    .getUser();
        } else {
            throw new UserNotFoundException("User with " +
                    moduleRole + " role in module " + moduleId +
                    "not found");
        }
    }
}
