package uk.ac.sheffield.team_project_team_24.domain.assessment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "ASSESSMENT_STAGE_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStageLog {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Assessment assessment;

    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;

    @ManyToOne
    private User actedBy;

    private LocalDateTime changedAt;

    @Column(length = 2000)
    private String note;
}
