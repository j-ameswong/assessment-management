package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditModuleDTO {
    private String oldModuleCode;
    private String newModuleCode;
    private String moduleName;

    private Long moduleLeadId;
    private Long moduleModeratorId;
    private List<Long> staffIds;
}
