package uk.ac.sheffield.team_project_team_24.service;

import java.time.LocalDateTime;
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
    @Autowired
    private AssessmentStageLogRepository logRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String ASSESSMENT_NOT_FOUND = "Assessment does not exist";

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public Assessment createAssessment(Assessment newAssessment) {
        return assessmentRepository.save(newAssessment);
    }

    public List<Assessment> getAssessments() {
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

    public Assessment advanceStage(Long id, AssessmentStage assessmentStage, Long actorId, String note) {

        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assessment not found"));

        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        assessment.setAssessmentStage(assessmentStage);

        AssessmentStageLog log = new AssessmentStageLog();
        log.setAssessment(assessment);
        log.setAssessmentStage(assessmentStage);
        log.setActedBy(actor);
        log.setChangedAt(LocalDateTime.now());
        log.setNote(note);

        logRepository.save(log);
        assessmentRepository.save(assessment);

        return assessment;
    }

    public List<AssessmentStageLog> getHistory(Long id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assessment not found"));

        return logRepository.findByAssessmentOrderByChangedAtAsc(assessment);
    }

}
