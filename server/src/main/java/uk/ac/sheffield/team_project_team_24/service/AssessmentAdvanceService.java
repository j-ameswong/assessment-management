package uk.ac.sheffield.team_project_team_24.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentRole;

@Service
public class AssessmentAdvanceService {
    private final AssessmentService assessmentService;
    private final UserService userService;

    public AssessmentAdvanceService(AssessmentService assessmentService,
            UserService userService) {
        this.assessmentService = assessmentService;
        this.userService = userService;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void updateAssessments() {
        List<Assessment> allAssessments = assessmentService.getAllAssessments();
        List<Assessment> assessmentsReqCheck = allAssessments
                .stream()
                .filter(a -> ((a.getDeadline() != null) || (a.getExamDate() != null)) &&
                        a.getAssessmentStage().getActor() == AssessmentRole.SYSTEM)
                .toList();

        System.out.println("Running assessmentAdvanceService...");
        for (Assessment a : assessmentsReqCheck) {
            String statusString = "Checking if assessment deadline/date has passed for assessment "
                    + a.getAssessmentName() + " with deadline/date of ";

            switch (a.getAssessmentType()) {
                case TEST:
                    LocalDateTime testDate = a.getExamDate();
                    System.out.println(statusString + testDate);
                    if (LocalDateTime.now().isAfter(testDate)) {
                        assessmentService.advanceStage(a.getId(), userService.getAdmin().getId(),
                                "The test has been held, progressed automatically by system", false);
                    }
                    break;
                case EXAM:
                    LocalDateTime examDate = a.getExamDate();
                    System.out.println(statusString + examDate);
                    if (LocalDateTime.now().isAfter(examDate)) {
                        assessmentService.advanceStage(a.getId(), userService.getAdmin().getId(),
                                "The exam has been held, progressed automatically by system", false);
                    }
                    break;
                case COURSEWORK:
                    LocalDateTime deadline = a.getDeadline();
                    System.out.println(statusString + deadline);
                    if (LocalDateTime.now().isAfter(deadline)) {
                        assessmentService.advanceStage(a.getId(), userService.getAdmin().getId(),
                                "Coursework deadline has passed, progressed automatically by system", false);
                    }
                    break;
                default:
                    System.out.println("Invalid assessment type...");
                    break;
            }
        }
    }
}
