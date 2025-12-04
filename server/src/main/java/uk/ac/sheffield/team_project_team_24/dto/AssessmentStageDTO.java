package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentRole;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStageDTO {
    private Long id;
    private AssessmentType assessmentType;
    private String stageName;
    private Long step;
    private AssessmentRole actor;

    public static AssessmentStageDTO fromEntity(AssessmentStage s) {
        return new AssessmentStageDTO(
                s.getId(),
                s.getAssessmentType(),
                s.getStageName(),
                s.getStep(),
                s.getActor());
    }

    // public Assessment toEntity() {
    // Assessment a = new Assessment();
    // return a;
    // }
    //
}
