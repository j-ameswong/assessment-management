package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;

@Service
@Transactional
public class AssessmentService {
  private final AssessmentRepository assessmentRepository;

  private static final String ASSESSMENT_NOT_FOUND = "Assessment does not exist";

  public AssessmentService(AssessmentRepository assessmentRepository) {
    this.assessmentRepository = assessmentRepository;
  }

  public void createAssessment(Assessment newAssessment) {
    assessmentRepository.save(newAssessment);
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
}
