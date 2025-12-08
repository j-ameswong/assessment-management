package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.exception.assessmentStageLog.AssessmentStageLogNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.dto.AssessmentDTO;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageLogRepository;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageLogService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageService;
import uk.ac.sheffield.team_project_team_24.exception.assessment.AssessmentNotFoundException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AssessmentServiceTest {

    // mocks
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    AssessmentStageLogRepository assessmentStageLogRepository;
    @Mock
    private AssessmentStageService assessmentStageService;
    @Mock
    private ModuleService moduleService;
    @Mock
    private UserService userService;
    @Mock
    private AssessmentStageLogService assessmentStageLogService;


    @InjectMocks
    private AssessmentService classUnderTest;

    // helpers
    // create dto
    private AssessmentDTO testAssessmentDTO() {
        AssessmentDTO dto = new AssessmentDTO();
        dto.setModuleId(1L);
        dto.setSetterId(2L);
        dto.setCheckerId(3L);
        dto.setType(AssessmentType.EXAM);
        dto.setName("Final Exam");
        return dto;
    }
    // create module
    private Module testModule() {
        return new Module("Test_Code", "Test_Module");
    }
    // create setter
    private User testSetter() {
        User setter = new User();
        setter.setId(2L);
        return setter;
    }
    // create checker
    private User testChecker() {
        User checker = new User();
        checker.setId(3L);
        return checker;
    }
    // create assessment
    private Assessment testAssessment1() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentType(AssessmentType.EXAM);
        return assessment;
    }
    private Assessment testAssessment2() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentType(AssessmentType.EXAM);
        return assessment;
    }
    private List<Assessment> testAssessmentList() {
        return Arrays.asList(testAssessment1(), testAssessment2());
    }

    // tests
    // create assessment with dto tests
    // success
    @Test
    void createAssessment_shouldCreateAssessmentWithCorrectFields() {
        // test classes
        AssessmentDTO dto = testAssessmentDTO();
        Module module = testModule();
        User setter = testSetter();
        User checker = testChecker();

        AssessmentStage firstStage = new AssessmentStage();

        // define behaviour
        when(moduleService.getModule(1L)).thenReturn(module);
        when(userService.getUser(2L)).thenReturn(setter);
        when(userService.getUser(3L)).thenReturn(checker);
        when(assessmentStageService.getFirstStage(AssessmentType.EXAM))
                .thenReturn(firstStage);

        // save should return the same assessment it was given
        when(assessmentRepository.save(any(Assessment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // call method under test
        Assessment result = classUnderTest.createAssessment(dto);

        // check returned values are as they should be
        assertEquals(module, result.getModule());
        assertEquals(setter, result.getSetter());
        assertEquals(checker, result.getChecker());
        assertEquals("Final Exam", result.getAssessmentName());
        assertEquals(AssessmentType.EXAM, result.getAssessmentType());
        assertEquals(firstStage, result.getAssessmentStage());

        // verify each method was only called once
        verify(moduleService, times(1)).getModule(1L);
        verify(userService, times(1)).getUser(2L);
        verify(userService, times(1)).getUser(3L);
        verify(assessmentStageService, times(1))
                .getFirstStage(AssessmentType.EXAM);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));

    }

    @Test
    void createAssessment_shouldAlwaysSetFirstStageRegardlessOfInput() {
        AssessmentDTO dto = testAssessmentDTO();
        Module module = testModule();
        User setter = testSetter();
        User checker = testChecker();
        AssessmentStage firstStage = new AssessmentStage();

        when(moduleService.getModule(1L)).thenReturn(module);
        when(userService.getUser(2L)).thenReturn(setter);
        when(userService.getUser(3L)).thenReturn(checker);
        when(assessmentStageService.getFirstStage(AssessmentType.EXAM)).thenReturn(firstStage);
        when(assessmentRepository.save(any(Assessment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // run method
        Assessment result = classUnderTest.createAssessment(dto);

        assertEquals(firstStage, result.getAssessmentStage());
        verify(assessmentStageService, times(1))
                .getFirstStage(AssessmentType.EXAM);

        verify(assessmentRepository, times(1))
                .save(any(Assessment.class));

        verifyNoMoreInteractions(
                assessmentStageService,
                assessmentRepository);
    }
    // no failure cases

    // create assessment(assessment) tests
    // success
    @Test
    void createAssessmentEntity_shouldSaveAssessment() {
        Assessment assessment = testAssessment1();

        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        Assessment result = classUnderTest.createAssessment(assessment);
        assertEquals(assessment, result);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
        verifyNoMoreInteractions(assessmentRepository);
    }
    // no failure cases

    // get assessments in module by module id tests
    // success
    @Test
    void getAssessmentsInModule_shouldReturnList_whenAssessmentsExist() {
        Module module = testModule();
        List<Assessment> assessments = testAssessmentList();

        // define behaviour
        when(assessmentRepository.findAllByModuleId(module.getId()))
                .thenReturn(Optional.of(assessments));

        List<Assessment> result = classUnderTest.getAssessmentsInModule(module.getId());

        assertEquals(assessments, result);
        verify(assessmentRepository, times(1)).findAllByModuleId(module.getId());
        verifyNoMoreInteractions(assessmentRepository);
    }

    // failure
    @Test
    void getAssessmentsInModule_shouldThrowException_whenNoAssessmentsExist() {
        when(assessmentRepository.findAllByModuleId(1L))
                .thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class,
                () -> classUnderTest.getAssessmentsInModule(1L));

        verify(assessmentRepository).findAllByModuleId(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // get all assessments tests
    // success
    @Test
    void getAllAssessments_shouldReturnList() {
        List<Assessment> assessments = testAssessmentList();
        when(assessmentRepository.findAll()).thenReturn(assessments);
        List<Assessment> result = classUnderTest.getAllAssessments();
        assertEquals(assessments, result);
        verify(assessmentRepository, times(1)).findAll();
        verifyNoMoreInteractions(assessmentRepository);
    }

    @Test
    void getAllAssessments_shouldReturnEmptyList_whenNoneExist() {
        when(assessmentRepository.findAll())
                .thenReturn(Collections.emptyList());
        List<Assessment> result = classUnderTest.getAllAssessments();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(assessmentRepository).findAll();
        verifyNoMoreInteractions(assessmentRepository);
    }

    // get assessment by id
    // success
    @Test
    void getAssessment_shouldReturnAssessment_whenExists() {
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        Assessment result = classUnderTest.getAssessment(1L);
        assertEquals(assessment, result);
        verify(assessmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // failure
    @Test
    void getAssessment_shouldThrowException_whenNotFound() {
        when(assessmentRepository.findAllByModuleId(1L))
                .thenReturn(Optional.empty());
        assertThrows(AssessmentNotFoundException.class,
        () -> classUnderTest.getAssessment(1L));
        verify(assessmentRepository, times(1)).findAllByModuleId(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // delete assessment by id tests
    // success
    @Test
    void deleteAssessment_shouldDelete_whenAssessmentExists() {
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        classUnderTest.deleteAssessment(1L);
        verify(assessmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // failure
    @Test
    void deleteAssessment_shouldThrow_whenAssessmentDoesNotExist() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AssessmentNotFoundException.class,
                () -> classUnderTest.deleteAssessment(1L));
        verify(assessmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // update assessment using dto
    // success
    @Test
    void updateAssessment_shouldUpdateNameAndType() {
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);
        AssessmentDTO dto = testAssessmentDTO();

        when(assessmentRepository.findById(1L))
                .thenReturn(Optional.of(assessment));
        when(assessmentRepository.save(any(Assessment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Assessment result = classUnderTest.updateAssessment(1L, dto);

        assertEquals(dto.getName(), result.getAssessmentName());
        assertEquals(dto.getType(), result.getAssessmentType());
        verify(assessmentRepository).findById(1L);
        verify(assessmentRepository, times(1)).save(assessment);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // advance stage tests
    // success
    @Test
    void advanceStage_shouldAdvanceStage() {
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);

        AssessmentStage currentStage = new AssessmentStage();
        AssessmentStage nextStage = new AssessmentStage();

        assessment.setAssessmentStage(currentStage);
        when(assessmentRepository.findById(1L))
                .thenReturn(Optional.of(assessment));
        when(assessmentStageService.getNextStage(currentStage))
                .thenReturn(nextStage);
        when(assessmentRepository.save(any(Assessment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(assessmentStageLogService)
                .generateLogFromAssessment(any(), anyLong(), any());

        Assessment result = classUnderTest.advanceStage(1L, 99L, "Advance to next stage");

        assertEquals(nextStage, result.getAssessmentStage());
        verify(assessmentRepository).findById(1L);
        verify(assessmentStageService).getNextStage(currentStage);
        verify(assessmentRepository).save(assessment);
        verify(assessmentStageLogService)
                .generateLogFromAssessment(assessment, 99L, "Advance to next stage");
        verifyNoMoreInteractions(
                assessmentRepository,
                assessmentStageService,
                assessmentStageLogService
        );
    }

    // failure
    @Test
    void advanceStage_shouldThrowException_whenAssessmentDoesNotExist() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AssessmentNotFoundException.class,
                () -> classUnderTest.advanceStage(1L, 99L, "Advance to next stage"));
        verify(assessmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // get history by id tests
    @Test
    void getHistoryById_shouldReturnLogs() {
        List<AssessmentStageLog> logs = Arrays.asList(new AssessmentStageLog(), new AssessmentStageLog());
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);

        when(assessmentStageLogRepository.findByAssessmentOrderByChangedAtAsc(assessment))
                .thenReturn(logs);
        List<AssessmentStageLog> result = classUnderTest.getHistory(1L);

        assertEquals(logs, result);
        verify(assessmentStageLogRepository, times(1)).findByAssessmentOrderByChangedAtAsc(assessment);
        verifyNoMoreInteractions(assessmentStageLogRepository);
    }

    // failure
    @Test
    void getHistoryById_shouldThrowException_whenAssessmentDoesNotExist() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AssessmentStageLogNotFoundException.class,
                () -> classUnderTest.getHistory(1L));
        verify(assessmentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentRepository);
    }

    // get history by assessment
    // success
    @Test
    void getHistoryByEntity_shouldReturnLogs() {
        List<AssessmentStageLog> logs = Arrays.asList(new AssessmentStageLog(), new AssessmentStageLog());
        Assessment assessment = testAssessment1();
        assessment.setAssessmentId(1L);
        when(assessmentStageLogRepository.findByAssessmentOrderByChangedAtAsc(assessment))
                .thenReturn(logs);
        List<AssessmentStageLog> result = classUnderTest.getHistory(assessment);
        assertEquals(logs, result);
        verify(assessmentStageLogRepository, times(1)).findByAssessmentOrderByChangedAtAsc(assessment);
        verifyNoMoreInteractions(assessmentStageLogRepository);
    }

    // save all
    // success
    @Test
    void saveAll_shouldReturnSavedList() {
        List<Assessment> assessments = testAssessmentList();
        List<Assessment> savedAssessments = classUnderTest.saveAll(assessments);
        assertEquals(savedAssessments, assessments);
        verify(assessmentRepository, times(1)).saveAll(assessments);
        verifyNoMoreInteractions(assessmentRepository);
    }


}