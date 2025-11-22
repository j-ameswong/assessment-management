package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.user.TeachingStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.TeachingStaffId;

public interface StaffRepository extends JpaRepository<TeachingStaff, TeachingStaffId> {

  // List<TeachingStaff> findByStaffIdUserId(Long userId);

  // List<TeachingStaff> findByStaffIDRoleName(String role);
  //
  // List<TeachingStaff> findByStaffIDModuleCode(String moduleCode);

}
