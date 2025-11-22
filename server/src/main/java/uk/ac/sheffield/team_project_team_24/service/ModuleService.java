package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;

@Service
@Transactional
public class ModuleService {
  private final ModuleRepository moduleRepository;

  private static final String MODULE_NOT_FOUND = "Module does not exist";

  public ModuleService(ModuleRepository moduleRepository) {
    this.moduleRepository = moduleRepository;
  }

  public void createModule(Module newModule) {
    moduleRepository.save(newModule);
  }

  public void createModules(List<Module> newModules) {
    moduleRepository.saveAll(newModules);
  }

  public List<Module> getModules() {
    return moduleRepository.findAll();
  }

  public Module getModule(String moduleCode) {
    return moduleRepository.findById(moduleCode)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MODULE_NOT_FOUND));
  }

  public void deleteModule(String moduleCode) {
    if (!moduleRepository.existsById(moduleCode)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, MODULE_NOT_FOUND);
    }
    moduleRepository.deleteById(moduleCode);
  }
}
