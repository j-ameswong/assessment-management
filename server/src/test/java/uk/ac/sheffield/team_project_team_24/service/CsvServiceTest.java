package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.repository.AssessmentRepository;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CsvServiceTest {

    @Autowired
    private CsvService csvService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ModuleStaffRepository moduleStaffRepository;


    // helper method to create users
    private User createTestUser(String forename,
                                String surname,
                                String email,
                                UserRole role) {

        User user = new User();
        user.setForename(forename);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword("test-password");
        user.setRole(role);
        user.setMustChangePassword(false);

        return userRepository.save(user);
    }

    @Test
    void testCsvImport() {

        // Create the users referenced in the CSV
        User lead   = createTestUser("testLead",  "User", "lead@test.com",  UserRole.ADMIN);
        User staff1 = createTestUser("testStaff1","User", "s1@test.com",    UserRole.ACADEMIC_STAFF);
        User staff2 = createTestUser("testStaff2","User", "s2@test.com",    UserRole.EXAMS_OFFICER);


        // CSV content
        String csvData =
                """
                moduleCode,moduleName,moduleLead,staffList,assessmentName, assessmentType
                COM2008,Software Engineering,testLead User,testStaff1 User;testStaff2 User,Programming Assignment, COURSEWORK
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvData.getBytes()
        );

        // Run the CSV parsing
        csvService.parse(file);

        // Validate the module creation
        Module module = moduleRepository.findByModuleCode("COM2008")
                .orElseThrow(() -> new RuntimeException("Module not created"));

        assertEquals("Software Engineering", module.getModuleName());


        // Validate staff are saved correctly
        List<ModuleStaff> staffList = moduleStaffRepository.findByModule(module);
        assertEquals(3, staffList.size());


        // Validate assessment creation
        List<Assessment> assessments = assessmentRepository.findByModule(module);
        assertEquals(1, assessments.size());

        Assessment a = assessments.get(0);
        assertEquals("Programming Assignment", a.getAssessmentName());
        assertEquals(AssessmentType.COURSEWORK, a.getAssessmentType());
    }
}
