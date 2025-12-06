package uk.ac.sheffield.team_project_team_24.domain.assessment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "AssessmentStageLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStageLog {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessmentId")
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentStageId")
    private AssessmentStage assessmentStage;

    @ManyToOne
    private User actedBy;

    private LocalDateTime changedAt;

    private Boolean isComplete;

    private String note;
}
