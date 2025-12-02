package uk.ac.sheffield.team_project_team_24.dto;

import lombok.Data;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;

@Data
public class CreateAssessmentDTO {
    private String name;
    private String description;
    private AssessmentType type;
}
