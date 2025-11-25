package uk.ac.sheffield.team_project_team_24.domain.assessment;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @OneToMany
    private List<Assessment> assessment;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    private String stageName;

    private int step;
}
