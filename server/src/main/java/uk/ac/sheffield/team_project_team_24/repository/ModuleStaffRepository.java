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
    Optional<List<ModuleStaff>> findByModuleId(@Param("moduleId") Long moduleId);

    @Query("SELECT ms FROM ModuleStaff ms WHERE ms.id.staffId = :userId")
    Optional<List<ModuleStaff>> findByStaffId(@Param("userId") Long userId);

    // list all staff per ModuleRole
    ModuleStaff findByModuleRole(ModuleRole moduleRole);

    // find all staff in this module
    List<ModuleStaff> findByModule(Module module);

    Optional<ModuleStaff> findByUserAndModule(User user, Module module);

    // this query works because staff can only have one role per module
    Optional<ModuleStaff> findFirstByModuleRoleAndModuleId(
            ModuleRole moduleRole, Long moduleId);
}
