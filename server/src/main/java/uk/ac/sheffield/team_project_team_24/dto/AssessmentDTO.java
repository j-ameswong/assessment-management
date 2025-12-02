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
public class AssessmentDTO {
    private Long id;
    private String name;
    private AssessmentType type;
    private AssessmentStage assessmentStage;
    private Long moduleId;
    private Long setterId;
    private Long checkerId;

    public static AssessmentDTO fromEntity(Assessment a) {
        return new AssessmentDTO(
                a.getAssessmentId(),
                a.getAssessmentName(),
                a.getAssessmentType(),
                a.getAssessmentStage(),
                a.getModule() != null ? a.getModule().getId() : null,
                a.getSetter() != null ? a.getSetter().getId() : null,
                a.getChecker() != null ? a.getChecker().getId() : null);
    }

    public Assessment toEntity() {
        Assessment a = new Assessment();
        a.setAssessmentId(id);
        a.setAssessmentName(name);
        a.setAssessmentType(type);
        a.setAssessmentStage(assessmentStage);
        return a;
    }

}
