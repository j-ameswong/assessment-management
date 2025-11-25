package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceRequestDTO {
    private AssessmentStage assessmentStage;
    private String note;
}
