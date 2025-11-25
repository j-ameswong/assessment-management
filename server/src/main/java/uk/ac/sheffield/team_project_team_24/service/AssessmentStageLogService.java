package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageLogRepository;

@Service
@Transactional
public class AssessmentStageLogService {
    @Autowired
    private final AssessmentStageLogRepository assessmentStageLogRepository;

    public AssessmentStageLogService(AssessmentStageLogRepository assessmentStageLogRepository) {
        this.assessmentStageLogRepository = assessmentStageLogRepository;
    }

    public AssessmentStageLog createAssessmentStageLog(AssessmentStageLog assessmentStageLog) {
        return assessmentStageLogRepository.save(assessmentStageLog);
    }
}
