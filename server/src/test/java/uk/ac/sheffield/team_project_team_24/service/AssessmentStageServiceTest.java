package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.exception.assessment.AssessmentNotFoundException;
import uk.ac.sheffield.team_project_team_24.exception.assessmentStage.stepOutOfBoundsException;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentStageRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssessmentStageServiceTest {

    @Mock
    private AssessmentStageRepository assessmentStageRepository;

    @InjectMocks
    private AssessmentStageService classUnderTest;

    // helpers
    private List<AssessmentStage> stages() {
        return Arrays.asList(new AssessmentStage(), new AssessmentStage());
    }

    // tests
    // create assessment stage test
    // success only
    @Test
    void createAssessmentStage_shouldSaveAndReturnStage() {
        AssessmentStage assessmentStage = new AssessmentStage();

        when(assessmentStageRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssessmentStage returnedStage = classUnderTest.createAssessmentStage(assessmentStage);

        assertEquals(assessmentStage, returnedStage);
        verify(assessmentStageRepository, times(1)).save(assessmentStage);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get assessment stage by id tests
    // success
    @Test
    void getAssessmentStage_shouldReturnStage_whenFound() {
        AssessmentStage assessmentStage = new AssessmentStage();
        assessmentStage.setId(1);

        when(assessmentStageRepository.findById(1L)).thenReturn(Optional.of(assessmentStage));

        AssessmentStage returnedStage = classUnderTest.getAssessmentStage(1L);

        assertEquals(assessmentStage, returnedStage);
        verify(assessmentStageRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // failure
    @Test
    void getAssessmentStage_shouldThrowException_whenNotFound() {
        when(assessmentStageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class,
                () -> classUnderTest.getAssessmentStage(1L));

        verify(assessmentStageRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get all stages by type tests
    // success
    @Test
    void getAllStagesByType_shouldReturnStages_whenStagesExist() {
        List<AssessmentStage> stages = stages();
        AssessmentType assessmentType = AssessmentType.TEST;

        when(assessmentStageRepository.findAllByAssessmentType(assessmentType)).thenReturn(Optional.of(stages));

        List<AssessmentStage> returnedStages = classUnderTest.getAllStagesByType(assessmentType);

        assertEquals(stages, returnedStages);
        verify(assessmentStageRepository, times(1)).findAllByAssessmentType(assessmentType);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // failure
    @Test
    void getAllStagesByType_shouldThrowException_whenStagesNotExist() {
        when(assessmentStageRepository.findAllByAssessmentType(AssessmentType.TEST))
                .thenReturn(Optional.empty());

        assertThrows(AssessmentNotFoundException.class,
                () -> classUnderTest.getAllStagesByType(AssessmentType.TEST));

        verify(assessmentStageRepository, times(1)).findAllByAssessmentType(AssessmentType.TEST);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get all stages test
    // success only
    @Test
    void getAllStages_shouldReturnAllStages() {
        List<AssessmentStage> stages = stages();

        when(assessmentStageRepository.findAll()).thenReturn(stages);

        List<AssessmentStage> returnedStages = classUnderTest.getAllStages();

        assertEquals(stages, returnedStages);
        verify(assessmentStageRepository, times(1)).findAll();
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get first stage test
    // success only
    @Test
    void getFirstStage_shouldReturnFirstStage() {
        AssessmentType assessmentType = AssessmentType.TEST;
        AssessmentStage assessmentStage = new AssessmentStage();
        assessmentStage.setId(1);

        when(assessmentStageRepository.findFirstByAssessmentType(assessmentType)).thenReturn(assessmentStage);

        AssessmentStage returnedStage = classUnderTest.getFirstStage(assessmentType);

        assertEquals(assessmentStage.getId(), returnedStage.getId());
        verify(assessmentStageRepository, times(1)).findFirstByAssessmentType(assessmentType);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get next stage tests
    // success
    @Test
    void getNextStage_shouldReturnNextStage_whenNextStageExists() {
        AssessmentType assessmentType = AssessmentType.TEST;

        AssessmentStage currentStage = new AssessmentStage();
        currentStage.setStep(1L);
        currentStage.setAssessmentType(assessmentType);

        AssessmentStage nextStage = new AssessmentStage();
        nextStage.setStep(2L);
        nextStage.setAssessmentType(assessmentType);

        when(assessmentStageRepository
                .findByAssessmentTypeAndStep(assessmentType, 2L))
                .thenReturn(Optional.of(nextStage));

        AssessmentStage result = classUnderTest.getNextStage(currentStage);

        assertEquals(nextStage, result);

        verify(assessmentStageRepository, times(1))
                .findByAssessmentTypeAndStep(assessmentType, 2L);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // failure
    @Test
    void getNextStage_shouldThrowException_whenNextStageDoesNotExist() {
        AssessmentType assessmentType = AssessmentType.TEST;

        AssessmentStage currentStage = new AssessmentStage();
        currentStage.setStep(1L);
        currentStage.setAssessmentType(assessmentType);

        when(assessmentStageRepository.findByAssessmentTypeAndStep(assessmentType, 2L))
                .thenReturn(Optional.empty());

        assertThrows(stepOutOfBoundsException.class,
                () -> classUnderTest.getNextStage(currentStage));
        verify(assessmentStageRepository, times(1)).findByAssessmentTypeAndStep(assessmentType, 2L);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // get previous stage tests
    // success
    @Test
    void getPrevStage_shouldReturnPreviousStage_whenPreviousStageExists() {
        AssessmentType assessmentType = AssessmentType.TEST;

        AssessmentStage currentStage = new AssessmentStage();
        currentStage.setStep(3L);
        currentStage.setAssessmentType(assessmentType);

        AssessmentStage prevStage = new AssessmentStage();
        prevStage.setStep(2L);
        prevStage.setAssessmentType(assessmentType);

        when(assessmentStageRepository.findByAssessmentTypeAndStep(assessmentType, 2L))
                .thenReturn(Optional.of(prevStage));

        AssessmentStage result = classUnderTest.getPrevStage(currentStage);

        assertEquals(prevStage, result);
        verify(assessmentStageRepository, times(1)).findByAssessmentTypeAndStep(assessmentType, 2L);
        verifyNoMoreInteractions(assessmentStageRepository);
    }

    // failure
    @Test
    void getPrevStage_shouldThrowException_whenPreviousStageDoesNotExist() {
        AssessmentType assessmentType = AssessmentType.TEST;

        AssessmentStage currentStage = new AssessmentStage();
        currentStage.setStep(2L);
        currentStage.setAssessmentType(assessmentType);

        when(assessmentStageRepository.findByAssessmentTypeAndStep(assessmentType, 1L))
                .thenReturn(Optional.empty());

        assertThrows(stepOutOfBoundsException.class,
                () -> classUnderTest.getPrevStage(currentStage));

        verify(assessmentStageRepository, times(1)).findByAssessmentTypeAndStep(assessmentType, 1L);
        verifyNoMoreInteractions(assessmentStageRepository);
    }
}
