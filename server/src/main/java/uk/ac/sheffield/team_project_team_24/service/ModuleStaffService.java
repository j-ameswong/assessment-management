package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;

@Service
@Transactional
public class ModuleStaffService {

  private final ModuleStaffRepository moduleStaffRepository;

  public ModuleStaffService(ModuleStaffRepository moduleStaffRepository) {
    this.moduleStaffRepository = moduleStaffRepository;
  }

  public void assignModuleStaff(ModuleStaff moduleStaff) {
    moduleStaffRepository.save(moduleStaff);
  }

  public void assignModuleStaff(List<ModuleStaff> moduleStaff) {
    moduleStaffRepository.saveAll(moduleStaff);
  }
}

// TODO: ideas on what to implement:
// 1. get all users from a module by module id
// 2. get specific role of the module
// 3. get missing roles (no module lead, moderator, etc)
