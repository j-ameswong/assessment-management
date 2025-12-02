package uk.ac.sheffield.team_project_team_24.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaffId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {
    private String moduleCode;
    private String moduleName;
    private List<ModuleStaffId> moduleStaffIds;

    public static ModuleDTO fromEntity(Module m) {
        ModuleDTO dto = new ModuleDTO();
        dto.moduleCode = m.getModuleCode();
        dto.moduleName = m.getModuleName();
        dto.moduleStaffIds = m.getModuleStaff()
                .stream()
                .map(ms -> ms.getId())
                .toList();

        return dto;
    }
}

