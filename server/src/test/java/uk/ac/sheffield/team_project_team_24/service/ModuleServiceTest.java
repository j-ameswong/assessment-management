package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ModuleServiceTest {

    // dummy module
    private static final String DUMMY_MODULE_CODE1 = "test-module-code";
    private static final String DUMMY_MODULE_NAME1 = "test-module-name";
    private static final String DUMMY_MODULE_CODE2 = "test2-module-code";
    private static final String DUMMY_MODULE_NAME2 = "test2-module-name";

    // return modules
    private static Module dummyModule1(){
        return new Module(DUMMY_MODULE_CODE1, DUMMY_MODULE_NAME1);
    }
    private static Module dummyModule2(){
        return new Module(DUMMY_MODULE_CODE2, DUMMY_MODULE_NAME2);
    }

    // return list of modules
    private static List<Module> dummyModuleList(){
        return Arrays.asList(dummyModule1(), dummyModule2());
    }

    // mocks
    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleService classUnderTest;

    // tests
    // create module tests
    // success
    @Test
    void createModule_shouldReturnSavedModule_whenModuleIsCreated() {
        Module savedModule = dummyModule1();
        when(moduleRepository.save(any())).thenReturn(savedModule) ;
        Module result = classUnderTest.createModule(savedModule);
        assertEquals(savedModule, result);
        verify(moduleRepository, times(1)).save(any());
    }
    // no failure cases

    //get module by id tests
    // success
    @Test
    void getModule_shouldReturnModule_whenModuleExists() {
        Module savedModule = dummyModule1();
        savedModule.setId(1L);
        when(moduleRepository.findById(1L)).thenReturn(Optional.of(savedModule));
        Module result = classUnderTest.getModule(1L);
        assertEquals(savedModule, result);
        verify(moduleRepository, times(1)).findById(1L);
    }

    // failure
    @Test
    void getModule_shouldThrowException_whenModuleDoesNotExist() {
        when(moduleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> classUnderTest.getModule(1L));
        verify(moduleRepository, times(1)).findById(1L);
    }

    // get modules tests
    // success
    @Test
    void getModules_shouldReturnListOfModules_whenModulesExist() {
        List<Module> savedModules = dummyModuleList();
        when(moduleRepository.findAll()).thenReturn(savedModules);
        List<Module> result = classUnderTest.getModules();
        assertEquals(savedModules, result);
        verify(moduleRepository, times(1)).findAll();
    }

    // success with edge case
    // should return an empty list rather than throwing an exception or something like that
    @Test
    void getModules_shouldReturnEmptyList_whenNoModulesExist() {
        when(moduleRepository.findAll()).thenReturn(Collections.emptyList());
        List<Module> result = classUnderTest.getModules();
        assertEquals(Collections.emptyList(), result);
        verify(moduleRepository, times(1)).findAll();
    }

    // get module by module code tests
    // success
    @Test
    void getModuleByCode_shouldReturnModule_whenModuleExists() {
        Module savedModule = dummyModule1();
        when(moduleRepository.findByModuleCode(DUMMY_MODULE_CODE1)).thenReturn(Optional.of(savedModule));
        Module result = classUnderTest.getModule(DUMMY_MODULE_CODE1);
        assertEquals(savedModule, result);
        verify(moduleRepository, times(1)).findByModuleCode(DUMMY_MODULE_CODE1);
    }

    // failure
    @Test
    void getModuleByCode_shouldThrowResponseStatusException_whenModuleNotFound() {
        when(moduleRepository.findByModuleCode(DUMMY_MODULE_CODE1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> classUnderTest.getModule(DUMMY_MODULE_CODE1));
        verify(moduleRepository, times(1)).findByModuleCode(DUMMY_MODULE_CODE1);
    }

    // delete module tests
    // success
    @Test
    void deleteModule_shouldDeleteModule_whenModuleExists() {
        Module savedModule = dummyModule1();
        when(moduleRepository.findByModuleCode(DUMMY_MODULE_CODE1)).thenReturn(Optional.of(savedModule));
        classUnderTest.deleteModule(DUMMY_MODULE_CODE1);
        verify(moduleRepository, times(1)).findByModuleCode(DUMMY_MODULE_CODE1);
        verify(moduleRepository, times(1)).delete(savedModule);
    }

    // failure
    @Test
    void deleteModule_shouldThrowException_whenModuleNotFound() {
        when(moduleRepository.findByModuleCode(DUMMY_MODULE_CODE1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> classUnderTest.deleteModule(DUMMY_MODULE_CODE1));
        verify(moduleRepository, times(1)).findByModuleCode(DUMMY_MODULE_CODE1);
    }
}
