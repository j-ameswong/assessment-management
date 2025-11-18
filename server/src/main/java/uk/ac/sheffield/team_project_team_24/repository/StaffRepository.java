package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.Staff;
import uk.ac.sheffield.team_project_team_24.domain.StaffID;

public interface StaffRepository extends JpaRepository<Staff, StaffID> {

    List<Staff> findByStaffIDUserID(Long userID);

    List<Staff> findByStaffIDRoleName(String role);

    List<Staff> findByStaffIDModuleCode(String moduleCode);

}
