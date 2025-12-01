package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleStaffDTO {
    private Long moduleId;
    private Long staffId;
    private ModuleRole moduleRole;
}
