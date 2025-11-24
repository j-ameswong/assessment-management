package uk.ac.sheffield.team_project_team_24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;

import java.util.List;

public interface AssessmentStageLogRepository extends JpaRepository<AssessmentStageLog, Long> {
    List<AssessmentStageLog> findByAssessmentOrderByChangedAtAsc(Assessment assessment);

}
