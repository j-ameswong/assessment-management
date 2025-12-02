package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.CreateAssessmentDTO;
import uk.ac.sheffield.team_project_team_24.exception.assessment.AssessmentNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentStageService assessmentStageService;
    private final AssessmentStageLogService assessmentStageLogService;

    public Assessment createAssessment(CreateAssessmentDTO req) {
        Assessment a = new Assessment();
        a.setAssessmentName(req.getName());
        a.setDescription(req.getDescription());
        a.setAssessmentType(req.getType());
        return assessmentRepository.save(a);
    }

    public Assessment createAssessment(Assessment a) {
        return assessmentRepository.save(a);
    }



    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment getAssessment(Long assessmentId) {
        return assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
    }

    public void deleteAssessment(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new AssessmentNotFoundException(assessmentId);
        }
        assessmentRepository.deleteById(assessmentId);
    }

    public Assessment updateAssessment(Long id, AssessmentDTO dto) {

        Assessment a = getAssessment(id);

        a.setAssessmentName(dto.getName());
        a.setAssessmentType(dto.getType());
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

    public List<Assessment> saveAll(List<Assessment> assessments) {
        return assessmentRepository.saveAll(assessments);
    }

    public List<Assessment> getAll() {
        return assessmentRepository.findAll();
    }

}
