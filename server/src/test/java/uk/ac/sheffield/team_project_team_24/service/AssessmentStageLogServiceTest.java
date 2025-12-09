package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.exception.assessmentStageLog.AssessmentStageLogNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageLogRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssessmentStageLogServiceTest {

    // mocks
    @Mock
    private AssessmentStageLogRepository assessmentStageLogRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private AssessmentStageLogService classUnderTest;

    // helpers
    // create assessment
    private Assessment testAssessment1() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentType(AssessmentType.EXAM);
        return assessment;
    }
    // create list of logs
    private List<AssessmentStageLog> testLogList() {
        AssessmentStageLog log1 = new AssessmentStageLog();
        log1.setAssessment(testAssessment1());
        AssessmentStageLog log2 = new AssessmentStageLog();
        log2.setAssessment(testAssessment1());
        return Arrays.asList(log1, log2);
    }

    // tests
    // create assessment stage log test
    // success
    @Test
    void createAssessmentStageLog_shouldSaveAndReturnLog() {
        AssessmentStageLog assessmentStageLog = new AssessmentStageLog();

        when(assessmentStageLogRepository.save(assessmentStageLog)).thenReturn(assessmentStageLog);

        AssessmentStageLog result = classUnderTest.createAssessmentStageLog(assessmentStageLog);

        assertEquals(assessmentStageLog, result);
        verify(assessmentStageLogRepository, times(1)).save(assessmentStageLog);
    }

    // generate log from assessment test
    // success
    @Test
    void generateLogFromAssessment_shouldCreateAndSaveLog() {
        User savedUser = new User();
        Assessment assessment = testAssessment1();

        when(userService.getUser(1L)).thenReturn(savedUser);
        when(assessmentStageLogRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssessmentStageLog result =
                classUnderTest.generateLogFromAssessment(assessment, 1L, "test");

        assertEquals(assessment, result.getAssessment());
        assertEquals(savedUser, result.getActedBy());
        assertEquals("test", result.getNote());
        verify(assessmentStageLogRepository).save(any());
    }


    // get all logs test
    // success
    @Test
    void getAllLogs_shouldReturnLogs_whenLogsExist() {
        List<AssessmentStageLog> logs = testLogList();

        when(assessmentStageLogRepository.findAll()).thenReturn(logs);

        List<AssessmentStageLog> result = classUnderTest.getAllLogs();

        assertEquals(logs, result);
        verify(assessmentStageLogRepository, times(1)).findAll();
        verifyNoMoreInteractions(assessmentStageLogRepository);
    }

    // failure
    @Test
    void getAllLogs_shouldThrowException_whenLogsEmpty() {
        when (assessmentStageLogRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EmptyRepositoryException.class,
                () -> classUnderTest.getAllLogs());
        verify(assessmentStageLogRepository).findAll();
    }

    // get logs by assessment tests
    // success
    @Test
    void getLogs_shouldReturnLogs_whenLogsExist() {
        List<AssessmentStageLog> logs = testLogList();
        Assessment assessment = testAssessment1();

        when(assessmentStageLogRepository.findByAssessmentOrderByChangedAtAsc(assessment)).thenReturn(logs);

        List<AssessmentStageLog> result = classUnderTest.getLogs(assessment);

        assertEquals(logs, result);
        verify(assessmentStageLogRepository, times(1)).findByAssessmentOrderByChangedAtAsc(assessment);
        verifyNoMoreInteractions(assessmentStageLogRepository);
    }

    // failure
    @Test
    void getLogs_shouldThrowException_whenNoLogsFound() {
        when(assessmentStageLogRepository.findByAssessmentOrderByChangedAtAsc(any()))
                .thenReturn(Collections.emptyList());

        assertThrows(AssessmentStageLogNotFoundException.class,
                () -> classUnderTest.getLogs(testAssessment1()));
        verify(assessmentStageLogRepository).findByAssessmentOrderByChangedAtAsc(testAssessment1());
    }
}
