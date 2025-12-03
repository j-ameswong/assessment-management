package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStageDTO {
    private Long id;
    private AssessmentType assessmentType;
    private String stageName;
    private Long step;

    public static AssessmentStageDTO fromEntity(AssessmentStage s) {
        return new AssessmentStageDTO(
                s.getId(),
                s.getAssessmentType(),
                s.getStageName(),
                s.getStep());
    }

    // public Assessment toEntity() {
    // Assessment a = new Assessment();
    // return a;
    // }
    //
}
