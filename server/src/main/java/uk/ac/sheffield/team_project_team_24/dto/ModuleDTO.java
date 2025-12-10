package uk.ac.sheffield.team_project_team_24.dto;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {
    private Long id;
    private String moduleCode;
    private String moduleName;
    private List<ModuleStaffDTO> moduleStaff;

    public static ModuleDTO fromEntity(Module m) {
        ModuleDTO dto = new ModuleDTO();
        dto.id = m.getId();
        dto.moduleCode = m.getModuleCode();
        dto.moduleName = m.getModuleName();

        var staff = m.getModuleStaff();
        if (staff == null){
            dto.moduleStaff = Collections.emptyList();
        } else {
            dto.moduleStaff = staff.stream().map(ModuleStaffDTO::fromEntity).toList();
        }


        return dto;
    }
}
