package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleStaffDTO {
    private Long moduleId;
    private Long staffId;
    private ModuleRole moduleRole;

    public static ModuleStaffDTO fromEntity(ModuleStaff ms) {
        ModuleStaffDTO staffDTO = new ModuleStaffDTO();
        staffDTO.moduleId = ms.getId().getModuleId();
        staffDTO.staffId = ms.getId().getStaffId();
        staffDTO.moduleRole = ms.getModuleRole();

        return staffDTO;
    }
}
