package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uk.ac.sheffield.team_project_team_24.domain.module.*;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

public interface ModuleStaffRepository extends JpaRepository<ModuleStaff, ModuleStaffId> {
    @Query("SELECT ms FROM ModuleStaff ms WHERE ms.id.moduleId = :moduleId")
    public Optional<List<ModuleStaff>> findByModuleId(@Param("moduleId") Long moduleId);

    // list all staff per ModuleRole
    public ModuleStaff findByModuleRole(ModuleRole moduleRole);

    // find all staff in this module
    // public ModuleStaff findAllByModuleId(Long moduleId);

    public Optional<ModuleStaff> findByUserAndModule(User user, Module module);

    // this query works because staff can only have one role per module
    public Optional<ModuleStaff> findFirstByModuleRoleAndModuleId(
            ModuleRole moduleRole, Long moduleId);
}
