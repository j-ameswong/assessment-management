package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    private Long id;
    private String name;
    private AssessmentType type;
    private Boolean isComplete;
    private String description;
    private Long assessmentStageId;
    private Long moduleId;
    private Long setterId;
    private Long externalExaminerId;
    private Long checkerId;

    public static AssessmentDTO fromEntity(Assessment a) {
        return new AssessmentDTO(
                a.getId(),
                a.getAssessmentName(),
                a.getAssessmentType(),
                a.getIsComplete(),
                a.getDescription(),
                a.getAssessmentStage().getId(),
                a.getModule() != null ? a.getModule().getId() : null,
                a.getSetter() != null ? a.getSetter().getId() : null,
                a.getExternalExaminer() != null ? a.getExternalExaminer().getId() : null,
                a.getChecker() != null ? a.getChecker().getId() : null);
    }

    // public Assessment toEntity() {
    // Assessment a = new Assessment();
    // return a;
    // }
    //
}
