package uk.ac.sheffield.team_project_team_24.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Boolean isActive;
    private String description;
    private Long assessmentStageId;
    private Long moduleId;
    private Long setterId;
    private Long checkerId;
    private UserDTO externalExaminer;
    private LocalDateTime deadline;
    private LocalDateTime assessmentDate;


    public static AssessmentDTO fromEntity(Assessment a) {
        return new AssessmentDTO(
                a.getId(),
                a.getAssessmentName(),
                a.getAssessmentType(),
                a.getIsComplete(),
                a.getIsActive(),
                a.getDescription(),
                a.getAssessmentStage().getId(),
                a.getModule() != null ? a.getModule().getId() : null,
                a.getSetter() != null ? a.getSetter().getId() : null,
                a.getChecker() != null ? a.getChecker().getId() : null,
                a.getExternalExaminer() != null ? new UserDTO(a.getExternalExaminer()) : null,
                a.getDeadline(),
                a.getExamDate());
    }

    // public Assessment toEntity() {
    // Assessment a = new Assessment();
    // return a;
    // }
    //
}
