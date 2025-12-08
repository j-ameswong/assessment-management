package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleStaffDTO {
    private Long moduleId;
    private Long staffId;
    private String forename;
    private String surname;
    private String email;
    private ModuleRole moduleRole;

    public static ModuleStaffDTO fromEntity(ModuleStaff ms) {
        ModuleStaffDTO staffDTO = new ModuleStaffDTO();

        staffDTO.moduleId = ms.getId().getModuleId();
        staffDTO.staffId = ms.getId().getStaffId();

        staffDTO.forename = ms.getUser().getForename();
        staffDTO.surname = ms.getUser().getSurname();
        staffDTO.email = ms.getUser().getEmail();

        staffDTO.moduleRole = ms.getModuleRole();


        return staffDTO;
    }
}
