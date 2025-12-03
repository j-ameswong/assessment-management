package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;

public interface AssessmentStageRepository extends JpaRepository<AssessmentStage, Long> {
    Optional<AssessmentStage> findById(Long id);

    List<AssessmentStage> findAll();

    Optional<List<AssessmentStage>> findAllByAssessmentType(AssessmentType assessmentType);

    AssessmentStage findFirstByAssessmentType(AssessmentType assessmentType);

    Optional<AssessmentStage> findByAssessmentTypeAndStep(AssessmentType assessmentType,
            Long step);

}
