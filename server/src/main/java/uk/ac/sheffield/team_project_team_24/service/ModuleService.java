package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;

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
}
