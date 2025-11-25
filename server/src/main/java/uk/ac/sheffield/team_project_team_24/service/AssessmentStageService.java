package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageRepository;

@Service
@Transactional
public class AssessmentStageService {
    @Autowired
    private final AssessmentStageRepository assessmentStageRepository;

    public AssessmentStageService(AssessmentStageRepository assessmentStageRepository) {
        this.assessmentStageRepository = assessmentStageRepository;
    }

    public AssessmentStage createAssessmentStage(AssessmentStage assessmentStage) {
        return assessmentStageRepository.save(assessmentStage);
    }

    public AssessmentStage getFirstStage(AssessmentType assessmentType) {
        return assessmentStageRepository.findFirstByAssessmentType(assessmentType);
    }
}
