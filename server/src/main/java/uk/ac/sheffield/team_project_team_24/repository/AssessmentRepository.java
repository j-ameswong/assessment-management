package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Optional<Assessment> findById(Long id);

    Optional<List<Assessment>> findAllByModuleId(Long moduleId);
}
