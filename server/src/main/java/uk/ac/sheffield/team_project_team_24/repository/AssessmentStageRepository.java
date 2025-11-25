package uk.ac.sheffield.team_project_team_24.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;

public interface AssessmentStageRepository extends JpaRepository<AssessmentStage, Long> {
    Optional<AssessmentStage> findById(Long id);

}
