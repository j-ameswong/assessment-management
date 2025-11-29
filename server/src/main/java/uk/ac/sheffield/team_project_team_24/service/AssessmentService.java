package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageLogRepository;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
public class AssessmentService {
    @Autowired
    private final AssessmentRepository assessmentRepository;

    private final AssessmentStageService assessmentStageService;

    private final AssessmentStageLogService assessmentStageLogService;

    private final UserService userService;

    private static final String ASSESSMENT_NOT_FOUND = "Assessment does not exist";

    public AssessmentService(AssessmentRepository assessmentRepository,
            AssessmentStageService assessmentStageService,
            AssessmentStageLogService assessmentStageLogService,
            UserService userService) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentStageService = assessmentStageService;
        this.assessmentStageLogService = assessmentStageLogService;
        this.userService = userService;
    }

    public Assessment createAssessment(Assessment newAssessment) {
        return assessmentRepository.save(newAssessment);
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment getAssessment(Long assessmentId) {
        return assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ASSESSMENT_NOT_FOUND));
    }

    public void deleteAssessment(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ASSESSMENT_NOT_FOUND);
        }
        assessmentRepository.deleteById(assessmentId);
    }

    public Assessment updateAssessment(Long id, AssessmentDTO dto) {

        Assessment a = getAssessment(id);

        a.setAssessmentName(dto.getName());
        a.setAssessmentType(dto.getType());
        a.setAssessmentStage(dto.getAssessmentStage());

        return assessmentRepository.save(a);
    }

    public Assessment advanceStage(Long id,
            Long actorId, String note) {

        Assessment assessment = getAssessment(id);

        assessment.setAssessmentStage(
                assessmentStageService.getNextStage(assessment.getAssessmentStage()));
        assessmentRepository.save(assessment);

        assessmentStageLogService.generateLogFromAssessment(assessment, actorId, note);

        return assessment;
    }

    public List<AssessmentStageLog> getHistory(Long id) {
        return assessmentStageLogService.getLogs(getAssessment(id));
    }

    public List<AssessmentStageLog> getHistory(Assessment assessment) {
        return assessmentStageLogService.getLogs(assessment);
    }
}
