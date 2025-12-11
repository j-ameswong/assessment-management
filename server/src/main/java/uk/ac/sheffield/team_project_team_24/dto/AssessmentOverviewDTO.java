package uk.ac.sheffield.team_project_team_24.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentOverviewDTO {
    private List<ModuleDTO> modules;
    private List<AssessmentDTO> assessments;
    private List<AssessmentStageDTO> stages;

    public static AssessmentOverviewDTO combineEntities(
            List<ModuleDTO> moduleDTO, List<AssessmentDTO> assessmentDTOs, List<AssessmentStageDTO> stageDTOs) {
        AssessmentOverviewDTO dto = new AssessmentOverviewDTO();
        dto.modules = moduleDTO;
        dto.assessments = assessmentDTOs;
        dto.stages = stageDTOs;

        return dto;
    }
}
