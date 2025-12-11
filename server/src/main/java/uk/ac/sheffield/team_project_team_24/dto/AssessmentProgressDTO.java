package uk.ac.sheffield.team_project_team_24.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentProgressDTO {
    private AssessmentDTO assessment;
    private ModuleDTO module;
    private List<AssessmentStageDTO> assessmentStages;
    private List<AssessmentStageLogDTO> assessmentStageLogs;
    private UserDTO examsOfficer;
    private UserDTO system;

    public static AssessmentProgressDTO from(
            List<AssessmentStageDTO> assessmentStages,
            List<AssessmentStageLogDTO> assessmentStageLogs,
            AssessmentDTO assessment,
            ModuleDTO module,
            UserDTO examsOfficer,
            UserDTO system) {
        AssessmentProgressDTO dto = new AssessmentProgressDTO();
        dto.assessment = assessment;
        dto.module = module;
        dto.assessmentStages = assessmentStages;
        dto.assessmentStageLogs = assessmentStageLogs;
        dto.examsOfficer = examsOfficer;
        dto.system = system;

        return dto;
    }
}
