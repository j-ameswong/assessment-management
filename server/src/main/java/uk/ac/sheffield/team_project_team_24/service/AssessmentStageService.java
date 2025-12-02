package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
import uk.ac.sheffield.team_project_team_24.exception.assessment.AssessmentNotFoundException;
import uk.ac.sheffield.team_project_team_24.exception.assessmentStage.stepOutOfBoundsException;
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

    public List<AssessmentStage> getAllStagesByType(AssessmentType assessmentType) {
        return assessmentStageRepository.findAllByAssessmentType(assessmentType)
                .orElseThrow(() -> new AssessmentNotFoundException(Long.valueOf(1)));
    }

    public AssessmentStage getFirstStage(AssessmentType assessmentType) {
        return assessmentStageRepository.findFirstByAssessmentType(assessmentType);
    }

    public AssessmentStage getNextStage(AssessmentStage currentStage) {
        Long currentStep = currentStage.getStep();
        AssessmentType type = currentStage.getAssessmentType();

        Optional<AssessmentStage> nextStage = assessmentStageRepository.findByAssessmentTypeAndStep(
                type, currentStep + 1);

        if (nextStage.isPresent()) {
            return nextStage.get();
        } else {
            throw new stepOutOfBoundsException(
                    "No step exists beyond step " + currentStep +
                            " for AssessmentType " + type);
        }
    }

    public AssessmentStage getPrevStage(AssessmentStage currentStage) {
        Long currentStep = currentStage.getStep();
        AssessmentType type = currentStage.getAssessmentType();

        Optional<AssessmentStage> nextStage = assessmentStageRepository.findByAssessmentTypeAndStep(
                type, currentStep - 1);

        if (nextStage.isPresent()) {
            return nextStage.get();
        } else {
            throw new stepOutOfBoundsException(
                    "No step exists before step " + currentStep +
                            " for AssessmentType " + type);
        }
    }
}
