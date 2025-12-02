package uk.ac.sheffield.team_project_team_24.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.exception.assessmentStageLog.AssessmentStageLogNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageLogRepository;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
public class AssessmentStageLogService {
    @Autowired
    private final AssessmentStageLogRepository assessmentStageLogRepository;

    @Autowired
    private final UserRepository userRepository;

    public AssessmentStageLogService(AssessmentStageLogRepository assessmentStageLogRepository,
            UserRepository userRepository) {
        this.userRepository = userRepository;
        this.assessmentStageLogRepository = assessmentStageLogRepository;
    }

    public AssessmentStageLog createAssessmentStageLog(AssessmentStageLog assessmentStageLog) {
        return assessmentStageLogRepository.save(assessmentStageLog);
    }

    public AssessmentStageLog generateLogFromAssessment(Assessment assessment, Long actorId,
            String note) {
        AssessmentStageLog log = new AssessmentStageLog();
        log.setAssessment(assessment);
        log.setAssessmentStage(assessment.getAssessmentStage());
        log.setActedBy(new UserService(userRepository)
                .getUser(actorId));
        log.setChangedAt(LocalDateTime.now());
        log.setNote(note);

        return assessmentStageLogRepository.save(log);
    }

    public List<AssessmentStageLog> getAllLogs() {
        List<AssessmentStageLog> logs = assessmentStageLogRepository.findAll();
        if (logs.isEmpty()) {
            throw new EmptyRepositoryException("AssesmentStageLogRepository");
        } else {
            return logs;
        }
    }

    public List<AssessmentStageLog> getLogs(Assessment assessment) {
        List<AssessmentStageLog> logs = assessmentStageLogRepository
                .findByAssessmentOrderByChangedAtAsc(assessment);
        if (logs.isEmpty()) {
            throw new AssessmentStageLogNotFoundException(
                    "No logs found for assessment " + assessment.getAssessmentId());
        } else {
            return logs;
        }
    }
}
