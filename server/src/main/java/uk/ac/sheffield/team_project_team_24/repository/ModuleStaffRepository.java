package uk.ac.sheffield.team_project_team_24.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team_project_team_24.domain.module.*;

public interface ModuleStaffRepository extends JpaRepository<ModuleStaff, ModuleStaffId> {
    public ModuleStaff findByModuleRole(ModuleRole moduleRole);

    public ModuleStaff findAllByModuleId(Long moduleId);

    public Optional<ModuleStaff> findFirstByModuleRoleAndModuleId(ModuleRole moduleRole, Long moduleId);
}
