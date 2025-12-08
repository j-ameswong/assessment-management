package uk.ac.sheffield.team_project_team_24.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentRole;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.UserSource;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentProgressDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentStageDTO;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentStageLogDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.exception.assessment.AssessmentNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentStageService assessmentStageService;
    private final AssessmentStageLogService assessmentStageLogService;
    private final ModuleService moduleService;
    private final UserService userService;

    public Assessment createAssessment(AssessmentDTO req) {
        Assessment a = new Assessment();
        Module m = moduleService.getModule(req.getModuleId());

        a.setModule(m);
        a.setSetter(userService.getUser(req.getSetterId()));
        a.setChecker(userService.getUser(req.getCheckerId()));
        a.setAssessmentType(req.getType());
        a.setAssessmentName(req.getName());
        // createAssessment should always set to first stage
        a.setAssessmentStage(assessmentStageService.getFirstStage(
                a.getAssessmentType()));

        createAssessment(a);
        log(a, userService.getAdmin(), "Initialized by system", false);

        return a;
    }

    public AssessmentProgressDTO getProgress(Long assessmentId) {
        Assessment a = getAssessment(assessmentId);
        List<AssessmentStage> assessmentStages = assessmentStageService
                .getAllStagesByType(a.getAssessmentType());
        List<AssessmentStageLog> assessmentStageLogs = assessmentStageLogService
                .getLogs(a);

        return AssessmentProgressDTO.from(
                assessmentStages.stream().map(s -> AssessmentStageDTO.fromEntity(s))
                        .toList(),
                assessmentStageLogs.stream().map(l -> AssessmentStageLogDTO.fromEntity(l))
                        .toList(),
                AssessmentDTO.fromEntity(a),
                ModuleDTO.fromEntity(a.getModule()),
                userService.getExamsOfficer().getId(),
                userService.getAdmin().getId());
    }

    // For when actor is not system
    public void log(Assessment a, User actor, String note, Boolean isComplete) {
        AssessmentStageLog log = new AssessmentStageLog();
        log.setAssessment(a);
        log.setAssessmentStage(a.getAssessmentStage());
        log.setActedBy(actor);
        log.setNote(note);
        log.setChangedAt(LocalDateTime.now());
        log.setIsComplete(isComplete);
        assessmentStageLogService.createAssessmentStageLog(log);
    }

    // For system only
    public void log(Assessment a) {
        AssessmentStageLog log = new AssessmentStageLog();
        log.setAssessment(a);
        log.setAssessmentStage(a.getAssessmentStage());
        log.setActedBy(userService.getAdmin());
        log.setNote("Action automatically taken by system");
        log.setChangedAt(LocalDateTime.now());
        log.setIsComplete(true);
        assessmentStageLogService.createAssessmentStageLog(log);

    }

    // For system with notes
    public void log(Assessment a, String note) {
        AssessmentStageLog log = new AssessmentStageLog();
        log.setAssessment(a);
        log.setAssessmentStage(a.getAssessmentStage());
        log.setActedBy(userService.getAdmin());
        log.setNote(note);
        log.setChangedAt(LocalDateTime.now());
        log.setIsComplete(true);
        assessmentStageLogService.createAssessmentStageLog(log);

    }

    public Assessment createAssessment(Assessment a) {
        assessmentRepository.save(a);

        return a;
    }

    public List<Assessment> getAssessmentsInModule(Long moduleId) {
        return assessmentRepository.findAllByModuleId(moduleId)
                // TODO: this exception has misleading id field in constructor, fix
                .orElseThrow(() -> new AssessmentNotFoundException(moduleId));
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
            Long actorId, String note, Boolean furtherActionReq) {

        Assessment assessment = getAssessment(id);
        log(assessment, userService.getUser(actorId), note, !furtherActionReq);

        if (furtherActionReq && !getHistory(id).get(-1).getIsComplete()) {
            assessment.setAssessmentStage(
                    assessmentStageService.getPrevStage(assessment.getAssessmentStage()));
            assessmentRepository.save(assessment);
        } else {
            assessment.setAssessmentStage(
                    assessmentStageService.getNextStage(assessment.getAssessmentStage()));
            assessmentRepository.save(assessment);

        }

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
