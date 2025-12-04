package uk.ac.sheffield.team_project_team_24.domain.assessment;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AssessmentStage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "assessmentStage", cascade = CascadeType.ALL)
    private List<Assessment> assessment;

    @OneToMany(mappedBy = "assessmentStage", cascade = CascadeType.ALL)
    private List<AssessmentStageLog> assessmentStageLogs;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    @Enumerated(EnumType.STRING)
    private AssessmentRole actor;

    private String stageName;

    private String description;

    private long step;
}
