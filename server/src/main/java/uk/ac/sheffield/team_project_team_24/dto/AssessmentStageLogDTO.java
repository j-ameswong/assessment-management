package uk.ac.sheffield.team_project_team_24.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStageLogDTO {
    private Long id;
    private Long assessmentId;
    private Long assessmentStageId;
    private Long actedById;
    private LocalDateTime changedAt;
    private Boolean isComplete;
    private String note; // feedback, response etc.

    public static AssessmentStageLogDTO fromEntity(AssessmentStageLog log) {
        AssessmentStageLogDTO dto = new AssessmentStageLogDTO();
        dto.id = log.getId();
        dto.assessmentId = log.getAssessment().getId();
        dto.assessmentStageId = log.getAssessmentStage().getId();
        dto.actedById = log.getActedBy().getId();
        dto.changedAt = log.getChangedAt();
        dto.isComplete = log.getIsComplete();
        dto.note = log.getNote();

        return dto;

    }
}
