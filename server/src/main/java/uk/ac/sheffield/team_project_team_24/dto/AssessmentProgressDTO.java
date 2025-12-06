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
    private Long examsOfficerId;
    private Long adminId;

    public static AssessmentProgressDTO from(
            List<AssessmentStageDTO> assessmentStages,
            List<AssessmentStageLogDTO> assessmentStageLogs,
            AssessmentDTO assessment,
            ModuleDTO module,
            Long examsOfficerId,
            Long adminId) {
        AssessmentProgressDTO dto = new AssessmentProgressDTO();
        dto.assessment = assessment;
        dto.module = module;
        dto.assessmentStages = assessmentStages;
        dto.assessmentStageLogs = assessmentStageLogs;
        dto.examsOfficerId = examsOfficerId;
        dto.adminId = adminId;

        return dto;
    }
}
